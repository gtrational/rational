import React, { Component } from 'react'
import { Switch, Route } from 'react-router-dom'

import Dashboard from './Dashboard'
import RatSightings from './RatSightings'
import RatMap from './RatMap'
import Graph from './Graph'

export default class LoggedIn extends Component {
    render() {
        return (
            <div>
                <Switch>
                    <Route exact path='/user' component={Dashboard} />
                    <Route path='/user/ratsightings' component={RatSightings} />
                    <Route path='/user/map' component={RatMap} />
                    <Route path='/user/graph' component={Graph} />
                </Switch>
            </div>
        )
    }
}