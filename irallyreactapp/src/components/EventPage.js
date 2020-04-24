import React, {Component} from 'react'
import "./EventPage.css"

class EventPage extends Component {
    render() {
        console.log("event: ", this.props.currEvent);
        return(
            <div class="card text-center">          
                this is the specific event page!
            </div>
        )
    }
}

export default EventPage;