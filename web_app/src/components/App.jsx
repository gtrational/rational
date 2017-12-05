import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';

import NotFound from './global/NotFound';
import Navigation from './global/Navigation';

import LoggedOut from './LoggedOut';
import Home from './LoggedOut/Home';

import LoggedIn from './LoggedIn';

import logo from '../logo.svg';
import './App.css';
import * as WebAPI from '../services/WebAPI';


function requireLoggedIn(nextState, replaceState) {
    // TODO: check logged in
}


export default class App extends Component {
    render() {
        return (
            <Router>
                <div className='App'>
                    <Navigation />
                    <header className="App-header">
                        <img src={logo} className="App-logo" alt="logo"/>
                        <h1 className="App-title">Welcome to Rational</h1>
                    </header>
                    <Switch>
                        <Route path='/user' component={LoggedIn} onEnter={requireLoggedIn}/>
                        <Route path='/' component={LoggedOut}/>
                        <Route path='*' component={NotFound}/>
                    </Switch>
                </div>
            </Router>
        );
    };
}

/*
new Promise(function (resolve, reject) {
    WebAPI.login("Oinkers", "ROOFTOWN")
        .then(function (response) {
            console.log(response);
            resolve(response);
        }, function (err) {
            reject(err);
            console.log(err);
        });
})
    .then(function () {
        WebAPI.getRatSightings(1000000000000, 100)
            .then(function (response) {
                console.log(response);
            }, function (err) {
                console.log(err);
            });
    });
*/