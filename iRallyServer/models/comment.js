const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const commentSchema = new Schema(
    {
        commentId: {
            type: String,
            required: true,
            unique: true,
            trim: true
        },
        author: {
            type: mongoose.Schema.Types.Mixed,
            // used mixed type as the author can be an admin or a user
            required: true,
        },
        event: {
            type: mongoose.Schema.Types.ObjectID,
            ref: 'event',
            required: true,
        },
        content: {
            type: String,
            required: true
        },
        replies: [
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

const Comment = mongoose.model('Comment', commentSchema);

module.exports = Comment;
