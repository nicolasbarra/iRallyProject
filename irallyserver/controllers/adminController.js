const {check, validationResult} = require('express-validator');
const session = require ("express-session");
const bcrypt = require('bcrypt');

const Admin = require('../models/admin');
const User = require('../models/user');

var currSession = null;
exports.create_admin = [
    (req, res) => {
        // make sure admin is not in database already
        Admin.findOne({'username': req.body.username}, (err, admin) => {
            if (err) {
                console.log('Unable to read item. Error JSON:', JSON.stringify(err, null, 2));
            } else {
                if (admin) {
                    // admin already exists
                    res.send({success: false});
                } else {
                    //store hashed password
                    bcrypt.genSalt(12, (err, salt) => {
                        if (err) {
                            console.log(err)
                        } else {
                            bcrypt.hash(req.body.password, salt, (err, hash) => {
                                if (err) {
                                    console.log(err);
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
                                            },
                                            numEventsCreated: 0
                                        }
                                    );

                                    admin.save((err, admin) => {
                                        if (err) {
                                            console.log('Unable to add item. Error JSON:', JSON.stringify(err, null, 2));
                                        } else {
                                            req.session.user = req.body.username;
                                            currSession = req.body.username;
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
        Admin.findOne({'username': req.body.username}, (err, admin) => {
            if (err) {
                console.log('Unable to read item. Error JSON:', JSON.stringify(err, null, 2));
            } else {
                if (admin) {
                    //compare hashed password to stored password which was hashed in the same way
                    bcrypt.compare(req.body.password, admin.password, (err, match) => {
                        if (err) {
                            console.log(err);
                            res.json({err: err});
                        } else {
                            if (match) {
                                req.session.user = req.body.username;
                                currSession = req.body.username;
                                console.log("LOGIN session", req.session);
                                req.session.save(() => {
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
        console.log("this is VALIDATE req.session.user: " , currSession);
        res.send({user: currSession, test: 'test'});
    }
];

exports.logout_admin = [
    (req, res) => {
        req.session.destroy((err) => {
            console.log('this is session that will be destroyed', req.session);
            if (err) {
                console.log(err);
                res.send({success: false});
            } else {
                console.log('session destroyed');
                currSession = null;
                res.send({success: true});
            }
        });
    }
];

exports.profile_admin = [
    (req, res) => {
        console.log("this is PROFILE req.session.user", req.session);
        Admin.findOne({'username': currSession}, (err, admin) => {
            res.send({
                status : 'Success',
                admin: admin
            });
         });
    }
];

exports.profile_admin_android = [
    (req, res) => {
        console.log("this is PROFILE req.session.user", req.session);
        Admin.findOne({'username': req.body.username}, (err, admin) => {
            console.log(admin);
            res.send({
                status : 'Success',
                admin: admin
            });
         });
    }
];

function getKeysWithHighestValue(o, n){
    var keys = Object.keys(o);
    keys.sort(function(a,b){
      return o[b] - o[a];
    })
    console.log(keys);
    return keys.slice(0,n);
}

exports.get_user_statistics = [
    (req, res) => {
        Admin.findOne({"username" : req.body.username}, (err, admin) => {
            if (err) {                                
                return res.json({
                    status: 'Failure',
                    errors: err,
                });
            } else if (admin) {       
                console.log("I HIT THIS")   
                let counter = 0;
                let followers = ["Wedddy", "xxxxxx", "redddy"] //should be admin.followers
                statistics = {
                    countMale: 0,
                    countFemale: 0,
                    interests: {},
                    highestInterestsKeys: [],
                    highestEventsAttendedKeys: [],
                    eventsAttended: {}
                }
                interests = {};
                eventsAttended = {};
                followers.forEach((follower) => {
                    console.log("this is follower: ", follower)
                    User.findOne({"username" : follower}, (err, user) => {
                        if (err) {
                            return res.json({
                                status: 'Failure',
                                errors: err,
                            });
                        } else if (user) {
                            counter++;
                            if (user.personalInfo.gender == "Male") {
                                statistics.countMale = statistics.countMale + 1;
                            } else {
                                statistics.countFemale = statistics.countFemale + 1;
                            }
                            user.personalInfo.interests.forEach((interest) => {
                                if (!interests[interest]) {
                                    interests[interest] = 1;
                                } else {
                                    interests[interest] = interests[interest] + 1;
                                }
                            }) 
                            // user.personalInfo.eventsAttended.forEach((event) => {
                            //     if (eventsAttended[event]) {
                            //         eventsAttended[event] = 1;
                            //     } else {
                            //         eventsAttended[event] = eventsAttended[event] + 1;
                            //     }
                            // })               
                                                
                            if (counter == followers.length) {           
                                let highestInterestsKeys = getKeysWithHighestValue(interests, 2);     
                                let highestEventsAttendedKeys = getKeysWithHighestValue(eventsAttended, 2);  
                                statistics.highestEventsAttendedKeys =  highestEventsAttendedKeys;
                                statistics.highestInterestsKeys = highestInterestsKeys
                                statistics.interests = interests;
                                statistics.eventsAttended = eventsAttended;           
                                return res.json({
                                    status: 'Success',
                                    statistics: statistics                              
                                });
                            }
                        } else {
                            return res.json({
                                status: 'Failure',
                                errors: 'No user with that username can be found.'
                            });
                        }
                    })     
                });
            } else {
                // there is no user with that username
                return res.json({
                    status: 'Failure',
                    errors: 'No user with that username can be found.'
                });
            }
        })
    }
]
