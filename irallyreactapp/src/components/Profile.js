import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import axios from 'axios';
import Loading from './Authorization/Loading';

class Profile extends Component {
    state = {
        currUser: null,
        loading: true
    }
    componentDidMount() {
        console.log('first');
        axios.get('http://localhost:9000/admins/') 
        .then (res => {
            console.log('SHOULD BE FIRSt!!')
            this.setState({
                currUser: res.data.admin,
                loading: false
            })            
        })     
      }

      render() {
        console.log('noo!!')
        if (!this.props.isAuthenticated) {
            return <Redirect to='/login'/>
        } else {
            if (this.state.loading) {
                return <Loading/>
            } else {
                return (
                    <div className="container-fluid">
                        <div>
                            Hello!
                            {/* {this.state.currUser.username}
                            {this.state.currUser.adminInfo.adminName}
                            {this.state.currUser.adminInfo.description}
                            {this.state.currUser.adminInfo.politicalAffiliation}
                            {this.state.currUser.adminInfo.goals}
                            {this.state.currUser.adminInfo.interests} */}
                        </div>
                    </div>
                )   
            }     
        } 
    }    
}

export default Profile;
