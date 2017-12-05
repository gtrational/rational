import React, { Component } from 'react'
import { Switch, Route, Link } from 'react-router-dom'

export default class Navigation extends Component {

    render() {
        return (
            <div>
                <Switch>
                    <Route path='/user'>
                        <ul>
                            <li><Link to='/user'>Dashboard</Link></li>
                            <li><Link to='/user/ratsightings'>Rat Sightings</Link></li>
                            <li><Link to='/user/map'>Map</Link></li>
                            <li><Link to='/user/graph'>Graph</Link></li>
                        </ul>
                    </Route>
                    
                    <Route path='/'>
                        <ul>
                            <li><Link to='/'>Home</Link></li>
                            <li><Link to='/register'>Register</Link></li>
                            <li><Link to='/login'>Login</Link></li>
                        </ul>
                    </Route>
                </Switch>
            </div>
        )
    }

}