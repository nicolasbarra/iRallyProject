import React, {Component} from 'react'
import axios from 'axios';
import {Redirect} from 'react-router-dom';

class Register extends Component {
    state = {
        firstName: null,
        lastsName: null,
        username: null,
        password: null,
        description: null,
        politicalAffiliation: 'Democrat',
        goals: null,
        interests: null                     
    };

    handleChange = (e) => {
        this.setState({
            [e.target.id]: e.target.value
        })
    };

    //axios and redirect
    handleSubmit = (e) => {
        e.preventDefault();
        axios.post('http://localhost:9000/admins/create', {
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            username: this.state.username,
            password: this.state.password,
            description: this.state.description,
            politicalAffiliation: this.state.politicalAffiliation,
            goals: this.state.goals,
            interests: this.state.interests  
        }).then(res => {
                if (res.data.err) {
                    alert('ERROR: ' + res.data.err);
                } else if (!res.data.success) {
                    this.props.changeState(false, null);
                    alert('that username has already been taken, please choose another username');
                } else {
                    this.props.changeState(true, this.state.username);
                    this.props.history.push('/');
                }
        })
    };

    render() {
        if (this.props.isAuthenticated || this.props.username) {
            return <Redirect to='/'/>
        }
        return (
            <div>
                <div className="container">
                    <h2>Sign Up</h2>
                    <form className="form-horizontal" onSubmit={this.handleSubmit}>
                        <div className="form-group row">
                            <label htmlFor="firstName" className="col-sm-3 control-label">First Name</label>
                            <div className="col-sm-9">
                                <input type="firstName" id="firstName" placeholder="Harry" className="form-control"
                                       name="firstName" onChange={this.handleChange} required/>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="lastName" className="col-sm-3 control-label">Last Name</label>
                            <div className="col-sm-9">
                                <input type="lastName" id="lastName" placeholder="Potter" className="form-control"
                                       name="lastName" onChange={this.handleChange}  required/>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="username" className="col-sm-3 control-label">Username</label>
                            <div className="col-sm-9">
                                <input type="text" id="username" placeholder="username"
                                       className="form-control"
                                       name="username" onChange={this.handleChange} required/>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="password" className="col-sm-3 control-label">Password</label>
                            <div className="col-sm-9">
                                <input type="password" id="password" placeholder="Password"
                                       className="form-control"
                                       name="password" onChange={this.handleChange} required/>
                            </div>
                        </div>                       
                        <div className="form-group row">
                            <label htmlFor="description" className="col-sm-3 control-label">Description</label>
                            <div className="col-sm-9">
                                <input type="text" id="description" placeholder="description..."
                                       className="form-control" name="description" onChange={this.handleChange} required/>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="goals" className="col-sm-3 control-label">Political Affiliation</label>
                            <div className="col-sm-3">
                                <select id="politicalAffiliation" onChange={this.handleChange} class="custom-select" required>
                                    <option value="Democrat">Democrat</option>
                                    <option value="Independent">Independent</option>
                                    <option value="Republican">Republican</option>
                                </select>
                            </div>
                        </div>
                        <div className="form-group row">
                            <label htmlFor="goals" className="col-sm-3 control-label">Goals</label>
                            <div className="col-sm-9">
                                <input type="text" id="goals" placeholder="goals..."
                                       className="form-control" name="goals" onChange={this.handleChange} required/>
                            </div>
                        </div> 
                        <div className="form-group row">
                            <label htmlFor="interests" className="col-sm-3 control-label">Interests</label>
                            <div className="col-sm-9">
                                <input type="text" id="interests" placeholder="interests..."
                                       className="form-control" name="interests" onChange={this.handleChange} required/>
                            </div>
                        </div>                 
                        <button type="submit" className="btn btn-primary btn-block">Sign Up</button>
                    </form>
                </div>
            </div>
        )
    }
}

export default Register;