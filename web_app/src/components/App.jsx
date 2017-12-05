import React, { Component } from 'react'
import { BrowserRouter as Router, Route, IndexRoute } from 'react-router-dom'

import NotFound from '../components/global/NotFound'

import LoggedOut from '../components/LoggedOut'
import Home from '../components/LoggedOut/Home'

import LoggedIn from '../components/LoggedIn'

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
                    <header className="App-header">
                        <img src={logo} className="App-logo" alt="logo" />
                        <h1 className="App-title">Welcome to React</h1>
                    </header>
                    <p className="App-intro">
                    To get started, edit <code>src/App.js</code> and save to reload.
                    </p>
                    
                    <Route path='/' component={LoggedOut}>
                        <IndexRoute component={Home} />
                    </Route>
                    <Route path='/user' component={LoggedIn} onEnter={requireLoggedIn} />
                    <Route path='*' component={NotFound} />
                </div>
            </Router>
        )
    }
}

new Promise(function(resolve, reject) {
    WebAPI.login("Oinkers", "ROOFTOWN")
        .then(function (response) {
            console.log(response)
            resolve(response)
        }, function(err) {
            reject(err)
            console.log(err)
        })
})
    .then(function(response) {
        WebAPI.getRatSightings(1000000000000,100)
            .then(function (response) {
                console.log(response)
            }, function(err) {
                console.log(err)
            })
    })

