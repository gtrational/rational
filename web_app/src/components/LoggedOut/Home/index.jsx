import React, { Component } from 'react'
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import AppBar from 'material-ui/AppBar';


export default class Home extends Component {
    render() {
        return (
        <div>
            <MuiThemeProvider>
                <AppBar
                    title="Home"
                />

            </MuiThemeProvider>
        </div>
        )
    }
}