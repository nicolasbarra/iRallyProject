import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import axios from 'axios';
import Loading from './Authorization/Loading';

class Profile extends Component {
    state = {
        currUser: null,
        loading: true,
        selectedFile: null,
        imageURL: "//placehold.it/150"
    }

    fileSelectedHandler = event => {
        this.setState({
            selectedFile: event.target.files[0]
        })
        console.log("this is selectedFile", this.state.selectedFile);
    }

    fileUploadHandler = event => {
        var formData = new FormData(); 
        formData.append("image", this.state.selectedFile); 
        formData.append("username", this.state.currUser.username);
        axios.post('http://localhost:9000/files/imageUploadAdmin', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }
       ).then (res => {
            this.setState({
                imageURL : res.data.imageURL
            })
        })     
    }

    componentDidMount() {
        console.log('first');
        axios.get('http://localhost:9000/admins/') 
        .then (res => {
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
                            <img src={this.state.imageURL} alt="avatar"/>
                            <h6>Profile Picture</h6>
                            <label class="custom-file">
                                <input type="file" onChange={this.fileSelectedHandler} />
                                <button onClick={this.fileUploadHandler}>Upload</button>
                            </label>
                        </div>
                        <div className="form-group row">
                                <label htmlFor="Name" className="col-sm-3 control-label">Name: {this.state.currUser.adminInfo.adminName}</label>
                        </div>
                        <div className="form-group row">
                                <label htmlFor="Description" className="col-sm-3 control-label">Description: {this.state.currUser.adminInfo.description}</label>
                        </div>   
                        <div className="form-group row">
                                <label htmlFor="PoliticalAffiliation" className="col-sm-3 control-label">Political Affiliation:   {this.state.currUser.adminInfo.politicalAffiliation}</label>
                        </div>   
                        <div className="form-group row">
                                <label htmlFor="Goals" className="col-sm-3 control-label">Goals:   {this.state.currUser.adminInfo.goals}</label>
                        </div>   
                        <div className="form-group row">
                                <label htmlFor="Interests" className="col-sm-3 control-label">Interests:   {this.state.currUser.adminInfo.interests}</label>
                        </div>                
                    </div>
                )   
            }     
        } 
    }    
    
}

export default Profile;
                    