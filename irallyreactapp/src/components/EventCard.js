import React, {Component} from 'react'
import {Link} from 'react-router-dom';
import "./EventCard.css"

class EventCard extends Component {
    // deleteEvent = () => {
    //     axios.post("http://localhost:9000/events/deleteEvent", {eventId : this.props.currEvent.eventId})
    //     .then((response) => {
    //         if (response.data.event) {
    //             console.log("event is deleted", response.data.event);
    //         }
    //     })
    // }

    render() {
        console.log("event: ", this.props.currEvent);
        return(
            <div class="card text-center">          
                <div class="card-body">
                    <h5 class="card-title">{this.props.currEvent.eventId}</h5>
                    <p class="card-text">{this.props.currEvent.description}</p>
                    <Link to={'/feed/' + this.props.currEvent.eventId}>
                        <button type="button" className="btn btn-primary">More Info</button>
                    </Link>                        
                </div>
                <div class="card-footer text-muted">
                    {this.props.currEvent.dateTime}
                </div>
            </div>
        )
    }
}

export default EventCard;