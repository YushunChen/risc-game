import React, { useState, useContext } from "react";
import { Card, Form, Button, Container } from "react-bootstrap";
import { Link } from 'react-router-dom';
import axios from "axios";
import Alert from 'react-bootstrap/Alert';
import { AuthContext } from "./AuthProvider";

const LoginView = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showAlert, setShowAlert] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);
  const { login } = useContext(AuthContext);

  const handleSignIn = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(`login`, {
        email: email,
        password: password
      });
      // console.log(response.data);
      setShowSuccess(true);
      login(response.data);
    } catch (error) {
      console.error(error);
      setShowAlert(true);
      setEmail('');
      setPassword('');
    }
  };

  return (
    <>
      {showAlert &&
        <Alert variant="danger" onClose={() => setShowAlert(false)} dismissible>
          <Alert.Heading>Oh snap! You got an error!</Alert.Heading>
          <p>
            Invalid email or password.
          </p>
        </Alert>
      }
      {showSuccess &&
        <Alert variant="success" onClose={() => setShowSuccess(false)} dismissible>
          <Alert.Heading>Welcome back to the RISC game!</Alert.Heading>
          <p>
            Good to see you again!
          </p>
        </Alert>
      }
      <Container className="vh-100 d-flex align-items-center justify-content-center">
        <Card style={cardStyles}>
          <Card.Body>
            <Card.Title className="text-center" style={titleStyles}>
              Sign In
            </Card.Title>
            <Form onSubmit={handleSignIn}>
              <Form.Group controlId="formEmail">
                <Form.Label style={textStyles}>Email</Form.Label>
                <Form.Control
                  type="email"
                  style={{ backgroundColor: "#e9f0fd" }}
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </Form.Group>

              <Form.Group controlId="formPassword">
                <Form.Label style={textStyles}>Password</Form.Label>
                <Form.Control
                  type="password"
                  style={{ backgroundColor: "#e9f0fd" }}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </Form.Group>
              <div className="text-center" style={{ marginTop: "5%" }} >
                <Button variant="primary" style={buttonStyles} size="lg" type="submit" block="true" >
                  Sign In
                </Button>
              </div>
            </Form>
            <p className="mt-3 mb-0 text-center" style={textStyles}>Don't have an account yet?{' '}
              <Link to="/register" style={{ color: "#77A6F7" }}>Register</Link> now.</p>
          </Card.Body>
        </Card>
      </Container >
    </>
  );
};

const cardStyles = {
  width: "30rem",
  height: "25rem",
  borderRadius: "2rem",
  boxShadow: "0 0 10px rgba(0, 0, 0, 0.6)",
};
const titleStyles = {
  fontSize: "2rem",
  color: "#77A6F7",
};
const textStyles = {
  color: "#BEBCBC"
}
const buttonStyles = {
  backgroundColor: "#77A6F7",
  fontWeight: "bold",
  outline: "none",
  border: "none",
  borderRadius: "40px",
  width: "8rem"
}

export default LoginView;
