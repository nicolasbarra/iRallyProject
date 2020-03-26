const {check, validationResult} = require('express-validator');
const bcrypt = require('bcrypt');

const Admin = require('../models/admin');

exports.create_admin = [
    (req, res) => {
        const varData = req.body.formData;
        // make sure admin is not in database already
        Admin.findOne({'username': varData.username}, (err, admin) => {
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
                            bcrypt.hash(varData.password, salt, (err, hash) => {
                                if (err) {
                                    debug(err);
                                    res.send({success: false});
                                } else {
                                    debug('Form data: ', varData);
                                    const admin = new Admin(
                                        {
                                            username: varData.username,
                                            password: hash,
                                            adminInfo: {
                                                adminName: varData.firstName + " " + varData.lastName,
                                                description: varData.description,
                                                politicalAffiliation: varData.politicalAffiliation,
                                                goals: varData.goals,
                                                interests: varData.interests,
                                            }
                                        }
                                    );

                                    admin.save((err, admin) => {
                                        if (err) {
                                            debug('Unable to add item. Error JSON:', JSON.stringify(err, null, 2));
                                        } else {
                                            console.log('Added item:', JSON.stringify(admin, null, 2));
                                            req.session.user = varData.username;
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
        const formData = req.body.formData;
        debug(formData);
        //ensure neither username nor password field was left blank
        if (!formData.username || !formData.password) {
            res.json({err: true});
        }
        Admin.findOne({'username': formData.username}, (err, admin) => {
            if (err) {
                debug('Unable to read item. Error JSON:', JSON.stringify(err, null, 2));
            } else {
                if (admin) {
                    debug("admin", admin);
                    //compare hashed password to stored password which was hashed in the same way
                    bcrypt.compare(formData.password, admin.password, (err, match) => {
                        if (err) {
                            debug(err);
                            res.json({err: err});
                        } else {
                            if (match) {
                                req.session.user = formData.username;
                                res.send({success: true, username: req.session.user});
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
