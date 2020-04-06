const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const session = require('express-session');
const bodyParser = require('body-parser');

// added for testing, may not need below
const cors = require("cors");

const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');
const adminsRouter = require('./routes/admins');
const eventsRouter = require('./routes/events');
const fileRouter = require('./routes/imageUpload');

const app = express();

app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());

app.use(session({
    secret: 'deourcm1232nscuo',
    resave: true,
    saveUninitialized: true
}));


// added for testing, may not need below
app.use(cors());

app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/admins', adminsRouter);
app.use('/admins', eventsRouter);
app.use('/files', fileRouter);
// catch 404 and forward to error handler
app.use(function (req, res, next) {
    next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
});

module.exports = app;
