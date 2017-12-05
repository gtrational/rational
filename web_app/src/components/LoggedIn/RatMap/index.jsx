import React, {Component} from 'react'

export default class RatMap extends Component {
    render() {
        return (
            <iframe src="http://gooz.ml/map.html" style={{position: 'relative', left: 0, top: 0, width: '100%', height: '500px'}}></iframe>
        )
    }
}