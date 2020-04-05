const {check, validationResult} = require('express-validator');
const bcrypt = require('bcrypt');

const Admin = require('../models/admin');

exports.create_admin = [
    (req, res) => {
        // make sure admin is not in database already
        Admin.findOne({'username': req.body.username}, (err, admin) => {
            if (err) {
                debug('Unable to read item. Error JSON:', JSON.stringify(err, null, 2));
            } else {
                if (admin) {
                    // admin already exists
                    res.send({success: false});
                } else {
                    //store hashed password
                    bcrypt.genSalt(12, (err, salt) => {
                        if (err) {
                            debug(err)
                        } else {
                            bcrypt.hash(req.body.password, salt, (err, hash) => {
                                if (err) {
                                    debug(err);
                                    res.send({success: false});
                                } else {
                                    const admin = new Admin(
                                        {
                                            username: req.body.username,
                                            password: hash,
                                            adminInfo: {
                                                adminName: req.body.firstName + " " + req.body.lastName,
                                                description: req.body.description,
                                                politicalAffiliation: req.body.politicalAffiliation,
                                                goals: req.body.goals,
                                                interests: req.body.interests,
                                            }
                                        }
                                    );

                                    admin.save((err, admin) => {
                                        if (err) {
                                            debug('Unable to add item. Error JSON:', JSON.stringify(err, null, 2));
                                        } else {
                                            debug('Added item:', JSON.stringify(admin, null, 2));
                                            req.session.user = req.body.username;
                                            res.send({success: true, username: req.session.user});
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        })
    }
];

exports.login_admin = [
    (req, res) => {
        //ensure neither username nor password field was left blank
        if (!req.body.username || !req.body.password) {
            res.json({err: true});
        }
        debug("LOGIN SUCCEEDED", req.body.username);
        Admin.findOne({'username': req.body.username}, (err, admin) => {
            if (err) {
                debug('Unable to read item. Error JSON:', JSON.stringify(err, null, 2));
            } else {
                if (admin) {
                    debug("admin", admin);
                    //compare hashed password to stored password which was hashed in the same way
                    bcrypt.compare(req.body.password, admin.password, (err, match) => {
                        if (err) {
                            debug(err);
                            res.json({err: err});
                        } else {
                            if (match) {
                                req.session.user = req.body.username;
                                debug("REQ.SESSION SUCCESS", req.session.user);
                                debug("REQ.SESSION SUCCESS2", req.session);
                                req.session.save(() => {
                                    debug("what the fck");
                                    res.send({success: true, username: req.session.user});
                                });
                            } else {
                                res.json({success: false});
                            }
                        }
                    });
                } else {
                    res.json({success: false});
                }
            }
        });
    }
];

exports.validate_login_admin = [
    (req, res) => {
        res.send({user: req.session.user, test: 'test'});
    }
];

exports.logout_admin = [
    (req, res) => {
        req.session.destroy((err) => {
            debug('this is session that will be destroyed', req.session);
            if (err) {
                debug(err);
                res.send({success: false});
            } else {
                debug('session destroyed');
                res.send({success: true});
            }
        });
    }
];

exports.profile_admin = [
    (req, res) => {
        debug("this is req.session.user", req.session);
        Admin.findOne({'username': req.session.user}, (err, admin) => {
            res.send({admin: admin});
        });
    }
];