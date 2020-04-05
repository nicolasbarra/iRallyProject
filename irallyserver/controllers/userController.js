const {check, validationResult} = require("express-validator");

const User = require('../models/user');

exports.create_user = [
    check('username').isString().notEmpty().trim().isLength({min: 3}).escape(),
    check('password').isString().notEmpty().trim().isLength({min: 3}).escape(),
    check('name').isString().notEmpty().trim().isLength({min: 3}).escape(),
    check('email').isString().notEmpty().trim().isLength({min: 3}).isEmail().normalizeEmail(),
    check('gender').notEmpty().escape(),
    check('genderPronouns').notEmpty().escape(),
    check('interests').isString().notEmpty().trim().isLength({min: 3}).escape(),
    (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.json({
                status: 'Failure',
                errors: errors.array()
            });
        } else {
            User.findOne({username: req.body.username}, (err, user) => {
                if (err) {
                    return res.json({
                        status: 'Failure',
                        errors: err
                    });
                } else if (user) {
                    // user with that username exists already
                    return res.json({
                        status: 'Failure',
                        errors: 'Username is taken, please try another.'
                    });
                } else {
                    // there is no user with that username, so we can safely save them to database
                    var interestList = req.body.interests.split(",");
                    const user = new User(
                        {
                            username: req.body.username,
                            password: req.body.password,
                            personalInfo: {
                                name: req.body.name,
                                email: req.body.email,
                                gender: req.body.gender,
                                genderPronouns: req.body.genderPronouns,
                                interests: interestList,
                            }
                        }
                    );
                    user.save((err) => {
                        if (err) {
                            return res.json({
                                status: 'Failure',
                                errors: err
                            });
                        } else {
                            return res.json({
                                status: 'Success',
                                errors: null
                            });
                        }
                    });
                }
            });
        }
    }
];

exports.delete_user = [
    check('username').isString().notEmpty().trim().isLength({min: 3}).escape(),
    (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.json({
                status: 'Failure',
                errors: errors.array()
            });
        } else {
            User.findOneAndDelete({username: req.body.username}, (err, user) => {
                if (err) {
                    return res.json({
                        status: 'Failure',
                        errors: err
                    });
                } else if (user) {
                    // user with that username exists
                    return res.json({
                        status: 'Success',
                        errors: null,
                        username: user.personalInfo.name
                    });
                } else {
                    // there is no user with that username
                    return res.json({
                        status: 'Failure',
                        errors: "Unable to delete: there is no user with that username"
                    });
                }
            });
        }
    }
];

exports.login_user = [
    check('username').isString().notEmpty().trim().isLength({min: 3}).escape(),
    check('password').isString().notEmpty().trim().isLength({min: 3}).escape(),
    (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.json({
                status: 'Failure',
                errors: errors.array(),
                passwordStatus: null
            });
        } else {
            User.findOne({username: req.body.username}, (err, user) => {
                if (err) {
                    return res.json({
                        status: 'Failure',
                        errors: err,
                        passwordStatus: null
                    });
                } else if (user) {
                    // user with that username exists
                    if (user.password === req.body.password) {
                        user.password = "";
                        return res.json({
                            status: 'Success',
                            errors: null,
                            passwordStatus: 'Correct',
                            user: user
                        });
                    } else {
                        return res.json({
                            status: 'Success',
                            errors: null,
                            passwordStatus: 'Incorrect'
                        });
                    }
                } else {
                    // there is no user with that username
                    return res.json({
                        status: 'Failure',
                        errors: 'No user with that username can be found.',
                        passwordStatus: null
                    });
                }
            });
        }
    }
];

exports.profile_user = [
    check('username').isString().notEmpty().trim().isLength({min: 3}).escape(),
    (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.json({
                status: 'Failure',
                errors: errors.array(),
            });
        } else {
            User.findOne({username: req.body.username}, (err, user) => {
                if (err) {
                    return res.json({
                        status: 'Failure',
                        errors: err,
                    });
                } else if (user) {
                    // user with that username exists
                    user.password = "";
                    return res.json(user);
                } else {
                    // there is no user with that username
                    return res.json({
                        status: 'Failure',
                        errors: 'No user with that username can be found.'
                    });
                }
            });
        }
    }
];