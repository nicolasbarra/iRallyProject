const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const eventSchema = new Schema(
    {
        eventId: {
            type: String,
            required: [true, 'event needs an id (should be unique)'],
            unique: true,
            trim: true
        },
        eventName: {
            type: String,
            required: [true, 'event needs a name'],
            trim: true,
            minlength: 3
        },
        creator: {
            type: mongoose.Schema.Types.Mixed,
            // used mixed type as the creator can be an admin or a user
            required: [true, 'events must have a creator']
        },
        description: {
            type: String,
            required: [true, 'events need descriptions'],
            minlength: 3
        },
        address: {
            type: String,
            required: [true, 'events need addresses']
        },
        dateTime: {
            type: Date,
            required: [true, 'events need a date and time']
        },
        attendees: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'user'
            }
        ],
        numberOfAttendees: {
            type: Number,
            required: [true, 'events must have number of attendees, may be zero']
        },
        interestsOfAttendees: [
            {
                type: String,
                required: [true, 'need to have some interest, start with those of creator']
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

module.exports = mongoose.model('Event', eventSchema);
