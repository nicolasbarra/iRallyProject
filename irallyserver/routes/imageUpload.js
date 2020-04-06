const express = require('express');
const router = express.Router();

const upload = require('../aws/fileUpload');
const Admin = require('../models/admin');

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

module.exports = router;

// TODO: move this file to index