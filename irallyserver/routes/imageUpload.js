const express = require('express');
const router = express.Router();

const upload = require('../aws/fileUpload');
const Admin = require('../models/admin');
const User = require('../models/user');

const singleUpload = upload.single('image');

router.post('/imageUploadAdmin', (req, res) => {
    singleUpload(req, res, (err) => {
        if (err) {
            console.log(err);
        }
        let username = req.body.username;     
        //, {$set: {'adminInfo.profilePictureLink': req.file.location}  
        Admin.findOne({'username' : username}, (err, admin) => {
            if (err) {
                return res.json({
                    status: 'Failure',
                    errors: err,
                });
            } else if (admin) {
                // user with that username exists
                admin.adminInfo.profilePictureLink = req.file.location;
                admin.save();
                return res.json({'imageURL' : req.file.location});
            } else {
                // there is no user with that username
                return res.json({
                    status: 'Failure',
                    errors: 'No user with that username can be found.'
                });
            }
        });      
    })
});

router.post('/imageUploadUser', (req, res) => {
    singleUpload(req, res, (err) => {
        if (err) {
            return res.json({
                status: 'Failure',
                errors: err,
            });
        } else {
            let username = req.body.username;
            User.findOne({'username' : username}, (err, user) => {
                if (err) {
                    return res.json({
                        status: 'Failure',
                        errors: err,
                        imageURL: null
                    });
                } else if (user) {
                    // user with that username exists
                    user.adminInfo.profilePictureLink = req.file.location;
                    user.save();
                    return res.json({
                        status: 'Success',
                        errors: null,
                        imageURL: req.file.location
                    });
                } else {
                    // there is no user with that username
                    return res.json({
                        status: 'Failure',
                        errors: 'No user with that username can be found.',
                        imageURL: null
                    });
                }
            });
        }
    })
});

module.exports = router;

// TODO: move this file to index