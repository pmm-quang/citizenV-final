import { useState } from 'react';
import './login.css';
import logo from './logo_login.png'
import 'bootstrap/dist/css/bootstrap.min.css';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import { Modal } from 'react-bootstrap';
import { AiOutlineUser, AiOutlineKey } from "react-icons/ai";
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Login() {

  let navigate = useNavigate()

  const [username, setUsername] = useState();
  const [password, setPassword] = useState();
  const [showNotifyLogin, setShowNotifyLogin] = useState(false)

  const CheckedLogin = async () => {
    try {
      const response = await axios.post("http://localhost:8080/api/v1/auth/login", {
        username: username,
        password: password
      })
      console.log(response.data)
      if (response.data !== null) {
        navigate('/home')
      }
      localStorage.setItem("user", JSON.stringify(response.data))
    } catch (error) {
      console.log(error.response)
      setShowNotifyLogin(true)
    }
  }

  const FailedLogin = () => {
    return (
      <Modal show={showNotifyLogin}>
        <Modal.Header className='headerModal'>
          <Modal.Title className='titleModal'>ĐĂNG NHẬP KHÔNG THÀNH CÔNG</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Label>Tên đăng nhập hoặc mật khẩu không chính xác</Form.Label>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => {
            setShowNotifyLogin(false)
          }}>
            Đóng
          </Button>
        </Modal.Footer>
      </Modal>
    )
  }

  return (
    <div className="App">
      <div className="Login">
        <div className="flex_login">
          <img src={logo} className='logoLogin' />
        </div>
        <div className="flex_login">
          <div className='formLogin'>
            <div className='titleLogin'>
              <p>Đăng nhập</p>
            </div>
            <div>
              <Form className='form'>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                  <p className='titleUser'>Tài khoản</p>
                  <InputGroup className="mb-3">
                    <InputGroup.Text id="basic-addon1" className="iconText"><AiOutlineUser /></InputGroup.Text>
                    <Form.Control
                      placeholder="Tên tài khoản"
                      type="text"
                      onChange={(e) => {
                        setUsername(e.target.value)
                      }}
                    />
                  </InputGroup>
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicPassword">
                  <p className='titleUser'>Mật khẩu</p>
                  <InputGroup className="mb-3">
                    <InputGroup.Text id="basic-addon1" className="iconText"><AiOutlineKey /></InputGroup.Text>
                    <Form.Control
                      placeholder="Mật khẩu"
                      type="password"
                      onChange={(e) => {
                        setPassword(e.target.value)
                      }}
                    />
                  </InputGroup>
                </Form.Group>
                <Button className='buttonLogin' onClick={() => CheckedLogin()}>
                  Đăng nhập
                </Button>
              </Form>
            </div>
          </div>
        </div>
      </div>
      <FailedLogin />
    </div>
  );
}

export default Login;
