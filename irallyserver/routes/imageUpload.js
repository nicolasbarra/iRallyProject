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
        Admin.findOneAndUpdate({'username' : username}, {profilePicURL: req.file.location});
        return res.json({'imageURL' : req.file.location});
    })
});

module.exports = router;

// TODO: move this file to index