const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const eventSchema = new Schema(
    {
        eventId: {
            type: String,
            required: true,
            unique: true,
            trim: true,
        },
        eventName: {
            type: String,
            required: true,
            trim: true,
            minlength: 3
        },
        creator: {
            type: mongoose.Schema.Types.ObjectID,
            ref: 'user',
            required: true,
        },
        description: {
            type: String,
            required: true,
            minlength: 3
        },
        address: {
            type: String,
            required: true
        },
        dateTime: {
            type: Date,
            required: true
        },
        attendees: [
            {
                type: mongoose.Schema.Types.ObjectID,
                ref: 'user'
            }
        ],
        numberOfAttendees: {
            type: Number,
            required: true,
        },
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

const Event = mongoose.model('Event', eventSchema);

module.exports = Event;
