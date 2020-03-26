const { check, validationResult } = require("express-validator");
const bcrypt = require("bcrypt");

const Admin = require('../models/admin');


exports.check_login = [
    (req, res) => {
        res.send({user: req.session.user, test:"test"});
    }
]

exports.add_admin = [
    (req, res) => {
        const varData = req.body.formData;
        //make sure user is not in table already
        Admin.findOne({"username" : varData.username}, function (err, admin) {
          if (err) {
            console.error("Unable to read item. Error JSON:", JSON.stringify(err, null, 2));
          } else {
            if (admin) {
              res.send({success: false});
            } else {   
              //store hashed password
              bcrypt.genSalt(10, function (err, salt) {
                if (err) {
                  console.log(err);
                } else {
                  bcrypt.hash(varData.password, salt, function (err, hash) {
                    if (err) {
                      console.log(err);
                      res.send({success: false});
                    } else {    
                      console.log("FORMDAtA: ", varData);
                        const newAdmin = new Admin(
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
                      
                        newAdmin.save(function(err, data) {
                            if (err) {
                              console.error("Unable to add item. Error JSON:", JSON.stringify(err, null, 2));
                            } else {
                              console.log("Added item:", JSON.stringify(data, null, 2));
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
]

exports.logout_admin = [
    (req, res) => {
        const temp = req.session.user;
        req.session.destroy(function (err) {
            console.log("this is session", req.session);
            if (err) {
            console.log(err);
            res.send({success: false});
            } else {
            console.log("session destroyed");
            res.send({success: true});
            }
        });
      }
]

exports.check_admin = [
    (req, res) => {
        console.log("CHECKEDDDDD USERR!!!!");
        const formData = req.body.formData;
        console.log(formData);
        //ensure neither username nor password field was left blank
        if (!formData.username || !formData.password) {
          res.json({err: true});
        }

        Admin.findOne({"username" : formData.username}, function (err, admin) {
            
            if (err) {
                console.error("Unable to read item. Error JSON:", JSON.stringify(err, null, 2));
            } else {
                if (admin) {            
                  console.log("admin", admin);
                  //compare hashed password to stored password which was hashed in the same way
                  bcrypt.compare(formData.password, admin.password, function (err, match) {
                    if (err) {
                      console.log(err);
                      res.json({err: err});
                    } else {
                      if (match) {                    
                          req.session.user = formData.username;
                          res.send({success: true, username: req.session.user});                     
                        } else {
                            res.json({success: false});
                        }
                      }
                    })
                  } else {
                    res.json({success: false});
                  }
            }
        })
    }
]





