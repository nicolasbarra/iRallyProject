const {check, validationResult} = require("express-validator");

const User = require('../models/user');
const Event = require('../models/event');
const Admin = require('../models/admin');

function getData(username) {
    return new Promise ((resolve, reject) => {
       User.find({username : username}, (err, user) => {
        if (err) {
            reject(res.json({
                status: 'Failure',
                errors: err,
            }))
        } else if (user) {
            resolve(user);
        } else {     
            reject(res.json({
                status: 'Failure',
                errors: 'No user with that username can be found.'
            }));
        }
       })
    })
}
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
                    const interestList = req.body.interests.split(',').map(x => x.trim());
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
                            },
                            numEventsCreated: 0
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
                    return res.json({
                        status: 'Success',
                        errors: null,
                        user: user
                    });
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

exports.add_interest = [
    check('username').isString().notEmpty().trim().isLength({min: 3}).escape(),
    check('interest').isString().notEmpty().trim().isLength({min: 3}).escape(),
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
                    let alreadyPresent = false
                    user.personalInfo.interests.forEach(x => {
                        if (x == req.body.interest) {
                            alreadyPresent = true
                        }
                    });
                    if (!alreadyPresent) {
                        user.personalInfo.interests.push(req.body.interest);
                        user.save();
                        return res.json({
                            status: 'Success',
                            errors: null,
                            newInterests: user.personalInfo.interests
                        });
                    } else {
                        return res.json({
                            status: 'Failure',
                            errors: `${req.body.interest} is already an interest.`,
                        });
                    }
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

exports.remove_interest = [
    check('username').isString().notEmpty().trim().isLength({min: 3}).escape(),
    check('interest').isString().notEmpty().trim().isLength({min: 3}).escape(),
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
                    const prevSize = user.personalInfo.interests.length;
                    user.personalInfo.interests = user.personalInfo.interests.filter(x => x != req.body.interest.trim());
                    if (prevSize === user.personalInfo.interests.length) {
                        return res.json({
                            status: 'Failure',
                            errors: 'Please enter one of your current interests in order to remove it.',
                        });
                    } else {
                        user.save();
                        return res.json({
                            status: 'Success',
                            errors: null,
                            newInterests: user.personalInfo.interests
                        });
                    }
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


exports.add_friend = [
    check('friendUsername').isString().notEmpty().trim().isLength({min: 3}).escape(),
    check('currUsername').isString().notEmpty().trim().isLength({min: 3}).escape(),
    (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.json({
                status: 'Failure',
                errors: errors.array(),
            });
        } else {
            User.find({"username" : { "$in" : [  
                req.body.currUsername,req.body.friendUsername ]}}, (err, users) => {
                //console.log("friendUsername1 ", req.body.friendUsername);
                if (err) {
                    return res.json({
                        status: 'Failure',
                        errors: err,
                    });
                } else if (users.length == 2) {
                    // user with that username exists
                    console.log("USERNAME: ", users[0].username, users[1].username);
                    let currUser = (users[0].username == req.body.currUsername) ? users[0] : users[1];
                    let friendUser = (users[0].username == req.body.friendUsername) ? users[0] : users[1];          
                    if (currUser.friends) {
                        currUser.friends.push(friendUser._id);
                        currUser.friendsString.push(friendUser.username);
                    } else {
                        currUser.friends = [friendUser._id];
                        currUser.friendsString = [friendUser.username];
                    }            

                    currUser.save(() => {
                        return res.json({
                            status: 'Success',
                            errors: null
                        });
                    });                 
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

exports.delete_friend = [
    check('friendUsername').isString().notEmpty().trim().isLength({min: 3}).escape(),
    check('currUsername').isString().notEmpty().trim().isLength({min: 3}).escape(),
    (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.json({
                status: 'Failure',
                errors: errors.array(),
            });
        } else {
            User.find({"username" : { "$in" : [  
                req.body.currUsername,req.body.friendUsername ]}}, (err, users) => {
                if (err) {
                    return res.json({
                        status: 'Failure',
                        errors: err,
                    });
                } else if (users.length == 2) {
                    // user with that username exists
                    let currUser = (users[0].username == req.body.currUsername) ? users[0] : users[1];
                    let friendUser = (users[0].username == req.body.friendUsername) ? users[0] : users[1];  
                    currUser.friends = currUser.friends.filter(x => x != friendUser._id);
                    currUser.friendsString = currUser.friendsString.filter(x => x != friendUser.username);
                    currUser.save();
                    return res.json({
                        status: 'Success',
                        errors: null
                    });
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

exports.get_event_feed = [
    check('username').isString().notEmpty().trim().isLength({min: 3}).escape(),
    (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.json({
                status: 'Failure',
                errors: errors.array(),
            });
        } else {
            eventsSend = [];
            User.findOne({"username": req.body.username}, (err, user) => {
                if (err) {
                    return res.json({
                        status: 'Failure',
                        errors: err,
                    });
                } else if (user) {
                    // user with that username exists                   
                     const friendsList = user.friendsString;
                    // async.forEach(friendsList, x => {
                    //         User.findOne({"username": x}, (err, friend) => {
                    //             if (err) {

                    //             } else {
                    //                 if (friend) {
                    //                     async.forEach(friend.eventsToAttendStrings, e => {
                    //                         console.log("in last loop");
                    //                         eventsSend.push(e)
                    //                     });

                    //                 } else {

                    //                 }
                    //             }
                    //         })
                    //     }
                    // );
                // friendsList.forEach(function(friend) {


                // })

                User.find({"username": {"$in": friendsList}}).exec(function (err, friends) {
                    let eventsList = [];
                    friends.forEach(function (friend) {
                        friend.eventsToAttendStrings.forEach(function (event) {
                            eventsList.push(event);
                        })
                    })
//                    console.log(eventsList);
                    return res.json({
                        status: 'Success',
                        errors: null,
                        eventsList: eventsList
                    });
                })

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

exports.follow_admin = [
    (req, res) => {
        console.log(req.body.username);
        console.log(req.body.admin);
        User.findOne({"username" : req.body.username}, (err, user) => {
            if (err) {
                return res.json({
                    status: 'Failure',
                    errors: err,
                });
            } else if (user) {
                console.log("this is user", user);
                if (user.adminsFollowed && !user.adminsFollowed.includes(req.body.admin)) {
                        Admin.findOne({"username" : req.body.admin}, (err, admin) => {
                            if (err) {
                                return res.json({
                                    status: 'Failure',
                                    errors: err,
                                });
                            } else if (admin){
                                user.adminsFollowed.push(admin._id);
                                if (admin.followers.includes(req.body.username)) {
                                    return res.json({
                                        status: 'Success',                                
                                    });
                                } else {
                                    admin.followers.push(req.body.username);
                                    user.save();
                                    admin.save();
                                    console.log("this is adminfollowers", admin.followers);
                                    return res.json({
                                        status: 'Success',                                
                                    });
                                }                                                             
                            } else {
                                return res.json({
                                    status: 'Failure',
                                    errors: 'No admin with that username can be found.'
                                });
                            }                         
                        })  
                } 
            } else {
                return res.json({
                    status: 'Failure',
                    errors: 'No user with that username can be found.'
                });
            }    
        })
    }
]






// search query: 

// get back all the list of all the users that match events that match and admins that match