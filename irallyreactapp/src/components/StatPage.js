import React, {Component} from 'react'
import "./EventPage.css"
import axios from "axios"
import {Redirect} from 'react-router-dom';
import Loading from './Authorization/Loading'
class StatPage extends Component {

    state = {
        statistics: null,
        loading: true
    }

    componentDidMount() {
        console.log("this is username:", this.props.username);
        axios.post("http://localhost:9000/admins/getUserStatistics", {username : this.props.username})
        .then((response) => {
            console.log("this is response", response.data.statistics);
            if (response.data.statistics) {
                this.setState({
                    statistics: response.data.statistics,
                    loading: false 
                })
            }
        })      
        // axios.post('http://localhost:9000/admins/') 
        // .then (res => {
        //     if (res.data.admin) {
        //         console.log("this is username:", res.data.admin.username);
                          
        //     }  
        // })     
    }

    render() {
        if (!this.props.isAuthenticated) {
            return <Redirect to='/login'/>
        } else {
            if (this.state.loading) {
                return <Loading/>
            } else {
            console.log("this is run");
                return(
                    <div className = "text-align: center">
                        <div class="card text-center">          
                          This is number of male followers: {this.state.statistics.countMale}
                        </div>
                        <div class="card text-center">          
                          This is number of female followers: {this.state.statistics.countFemale}
                        </div>
                        <div class="card text-center">          
                          This is percentage of male followers: {(this.state.statistics.countMale / (this.state.statistics.countFemale + this.state.statistics.countMale))*100} %
                        </div>
                        <div class="card text-center">          
                          This is percentage of female followers: {(this.state.statistics.countFemale / (this.state.statistics.countFemale + this.state.statistics.countMale))*100} %
                        </div>
                        <div class="card text-center">          
                          These are top 3 interests of followers: {this.state.statistics.highestInterestsKeys.map((interest, i) => <div>{(i+1) + " : " + interest}</div>)}
                        </div>
                        <div class="card text-center">          
                          These are top 3 events attended of followers: {this.state.statistics.highestEventsAttendedKeys.map((event, i) => <div>{(i+1) + " : " + event}</div>)}
                        </div>
                    </div>
                )
            }
        }
    }
}

export default StatPage;