const aws = require('aws-sdk');
const multer = require('multer');
const multerS3 = require('multer-s3');
 aws.config.update({
    secretAccessKey: "",
    accessKeyId: "",
    region: ''
 });
const s3 = new aws.S3();
 
const upload = multer({
  storage: multerS3({
    s3: s3,
    bucket: 'profilepicturesirally',
    acl: 'public-read',
    metadata: function (req, file, cb) {
      cb(null, {fieldName: 'META_DATA!'});
    },
    key: function (req, file, cb) {
      cb(null, Date.now().toString())
    }
  })
});
 
module.exports = upload;
