const { check, validationResult } = require("express-validator");

const User = require('../models/user');

exports.create_user = [
    check('username').isString().notEmpty().trim().isLength({ min: 3 }).escape(),
    check('password').isString().notEmpty().trim().isLength({ min: 3 }).escape(),
    check('name').isString().notEmpty().trim().isLength({ min: 3 }).escape(),
    check('email').isString().notEmpty().trim().isLength({ min: 3 }).isEmail().normalizeEmail(),
    check('gender').notEmpty().escape(),
    check('genderPronouns').notEmpty().escape(),
    check('interests').isString().notEmpty().trim().isLength({ min: 3 }).escape(),
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            // TODO: this may be incorrect
            return res.status(422).json({ errors: errors.array() });
        } else {
            // TODO: route content here
            User.findOne({ username: req.body.username}, (err, user) => {
                if (err) {
                    return next(err);
                } else if (user) {
                    // user with that username exists already
                    res.send('Username is taken, please try another');
                } else {
                    // there is no user with that username, so we can safely save them to database
                    const user = new User(
                        {
                            username: req.body.username,
                            password: req.body.password,
                            personalInfo: {
                                name: req.body.name,
                                email: req.body.email,
                                gender: req.body.gender,
                                genderPronouns: req.body.genderPronouns,
                                interests: ['default temporary interest'],
                            }
                        }
                    );
                    user.save( (err) => {
                        if (err) {
                            // TODO: this may be incorrect
                            res.type('html').status(200);
                            res.write('uh oh: ' + err);
                            console.log(err);
                            res.end();
                            return res;
                            // return next(err);
                        } else {
                            // TODO: this may be incorrect
                            res.render('created', {user : user});
                        }
                    });
                }
            });
        }
    }
];

exports.delete_user = [
    check('username').isString().notEmpty().trim().isLength({ min: 3 }).escape(),
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            // TODO: this may be incorrect
            return res.status(422).json({ errors: errors.array() });
        } else {
            // TODO: route content here
            User.findOneAndDelete({ username: req.body.username}, (err, user) => {
                if (err) {
                    return next(err);
                } else if (user) {
                    // user with that username exists already
                    res.send(user + "successfully deleted");
                } else {
                    // there is no user with that username
                    res.send("Unable to delete: there is no user with that username")
                }
            });
        }
    }
];

exports.login_user = [
    check('username').isString().notEmpty().trim().isLength({ min: 3 }).escape(),
    check('password').isString().notEmpty().trim().isLength({ min: 3 }).escape(),
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            // TODO: this may be incorrect
            return res.status(422).json({ errors: errors.array() });
        } else {
            // TODO: route content here
            User.findOne({ username: req.body.username}, (err, user) => {
                if (err) {
                    return next(err);
                } else if (user) {
                    // user with that username exists
                    if (user.password === req.body.password) {
                        res.send('Success! Password is correct.')
                    } else {
                        res.send('Incorrect password.');
                    }
                } else {
                    // there is no user with that username
                    res.send('Username does not match any account')
                }
            });
        }
    }
];
