const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const userSchema = new Schema(
    {
        username: {
            type: String,
            required: true,
            unique: true,
            trim: true,
            minlength: 3,
            lowercase: true
        },
        password: {
            type: String,
            required: true,
            trim: true,
            minlength: 3
        },
        personalInfo: {
            name: {
                type: String,
                required: true,
                trim: true,
                minlength: 3
            },
            email: {
                type: String,
                required: true,
                lowercase: true,
                trim: true,
                minlength: 3
            },
            gender: {
                type: String,
                required: true,
                trim: true
            },
            genderPronouns: {
                type: String,
                required: true,
                trim: true
            },
            profilePictureLink: String,
            interests: [String]
        },
        eventsToAttend: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'event'
            }
        ],
        eventsAttended: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'event'
            }
        ],
        adminsFollowed: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'admin'
            }
        ],
        friends: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'user'
            }
        ],
        comments: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'comment'
            }
        ]
    },
    {
        timestamps: true,
    }
);

const User = mongoose.model('User', userSchema);

module.exports = User;
