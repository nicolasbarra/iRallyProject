const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');

// added for testing, may not need below
const cors = require("cors");

// to get the password from the password file such that we do not push our
// password to git
const atlasdbAdminPassword = require('./atlasdbAdminPassword');

// for connection to mongoDB
const mongoose = require('mongoose');
const mongoDB = `mongodb+srv://atlasdbAdmin:${atlasdbAdminPassword.atlasdbAdminPassword}@cluster1-asdlf.mongodb.net/iRally?retryWrites=true&w=majority`;
mongoose.connect(mongoDB,
    {
      useNewUrlParser: true,
      useUnifiedTopology: true
    }
);
const db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
// TODO: check to see if the below needs to be removed or heavily edited for real use
db.once('open', function() {
  // we're connected!
  console.log("MongoDB database connection established successfully");
});

const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');

// testing, remove later
const testAPIRouter = require("./routes/testAPI");

const app = express();

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
