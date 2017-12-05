import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import AppBar from 'material-ui/AppBar';
import RaisedButton from 'material-ui/RaisedButton';
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';
import TextField from 'material-ui/TextField';

import {login} from '../../../services/WebAPI';

class Login extends Component {

  constructor(props) {
    super(props);
    this.state={
      username:'',
      password:'',
      error:'',
      redirect:false
    }
  }

  handleSubmit(e) {
    // something to handle submitting...
    // possibly with database stuff, I think
    console.log("values",this.state.username,this.state.password);

    if (this.state.username == '') {
      this.setState({error:"Please enter username"})
    } else if (this.state.password == '') {
      this.setState({error:"Please enter password"})
    } else {

      login(this.state.username, this.state.password)
        .then(response => {
            console.log(response) // success, error, permissionID,
            if (response.success) {
              this.setState({redirect: true})
            }
          }, err => {
              console.log(err)
              this.setState({error:"Invalid Username/Password"})
          })
    }

  }

  render() {
    if (this.state.redirect) {
      return <Redirect to='/user'/>
    }
    return (
      <div>
        <MuiThemeProvider>
        <AppBar
          title="Login"
        />

        <TextField
        hintText="Enter Username"
        floatingLabel="Username"
        onChange = {(event, newValue) => this.setState({username:newValue})}
        />
        <br/>

        <TextField
        hintText="Enter Password"
        floatingLabel="Password"
        type="password"
        onChange = {(event, newValue) => this.setState({password:newValue})}
        />
        <br/>
        <br/>

        <RaisedButton label="Submit" primary={true} onClick={this.handleSubmit.bind(this)} />
        {this.state.error}
        </MuiThemeProvider>
      </div>
    );
  }
}

export default Login;
