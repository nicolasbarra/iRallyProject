const aws = require('aws-sdk')
const multer = require('multer')
const multerS3 = require('multer-s3')
 aws.config.update({
    secretAccessKey: "BB2IEerAEOe+gP5/jL3ztAr0Pq73v0n67iL9LvS6",
    accessKeyId: "AKIAJMHJG5IZYFCOQA3A",
    region: 'us-east-1'
 })
const s3 = new aws.S3()
 
const upload = multer({
  storage: multerS3({
    s3: s3,
    bucket: 'profilepicturesirally',
    acl: 'public-read',
    metadata: function (req, file, cb) {
      cb(null, {fieldName: 'TESTING_META_DATA!'});
    },
    key: function (req, file, cb) {
      cb(null, Date.now().toString())
    }
  })
})
 
module.exports = upload;