class FlagLogo extends React.Component {
    render() {
        let flagged = this.props.flagged;
        if (flagged) {
            return (<img    src = "/blog/images/flag-logo-red-fill.svg"
                            className = "thumb-logo"
                    />);
          }
          else {
            return (<img    src = "/blog/images/flag-logo-no-fill.svg"
                            className = "thumb-logo"
                    />);
          }
    }
  }
  
  