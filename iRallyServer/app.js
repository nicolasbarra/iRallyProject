var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

// added for testing, may not need below
var cors = require("cors");

// for connection to mongoDB
var mongoose = require('mongoose');
var mongoDB = "mongodb+srv://atlasdbAdmin:<password>@cluster1-asdlf.mongodb.net/irally?retryWrites=true&w=majority";
mongoose.connect('mongodb://localhost/test', {useNewUrlParser: true});
var db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function() {
  // we're connected!
  console.log("MongoDB database connection established successfully");
});

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

// testing, remove later
var testAPIRouter = require("./routes/testAPI");

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// added for testing, may not need below
app.use(cors());

app.use('/', indexRouter);
app.use('/users', usersRouter);

// testing, remove later
app.use("/testAPI", testAPIRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
