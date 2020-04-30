const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const userSchema = new Schema(
    {
        username: {
            type: String,
            required: [true, 'need a username'],
            unique: true,
            trim: true,
            minlength: 3,
            lowercase: true
        },
        password: {
            type: String,
            required: [true, 'need a password'],
            trim: true,
            minlength: 3
        },
        personalInfo: {
            name: {
                type: String,
                required: [true, 'need a name'],
                trim: true,
                minlength: 3
            },
            email: {
                type: String,
                required: [true, 'need an email'],
                lowercase: true,
                trim: true,
                minlength: 3
            },
            gender: {
                type: String,
                required: [true, 'need to give some gender'],
                trim: true
            },
            genderPronouns: {
                type: String,
                required: [true, 'need to give some gender pronoun'],
                trim: true
            },
            interests: [
                {
                    type: String,
                    required: [true, 'need to have some interest']
                }
            ],
            profilePictureLink: String
        },
        numEventsCreated: {
            type: Number,
            required: [true, 'set eventsCreated to zero when creating user']
        },
        eventsCreatedRefs: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'event'
            }
        ],
        eventsCreatedStrings: [
            {
                type: String
            }
        ],
        eventsToAttendRefs: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'event'
            }
        ],
        eventsToAttendStrings: [
            {
                type: String
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
        adminsFollowedStrings: [
            {
                ref: String
            }
        ],
        friends: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'user'
            }
        ],
        friendsString:[
            {
                type: String
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

module.exports = mongoose.model('User', userSchema);
