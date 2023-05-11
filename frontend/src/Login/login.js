import './login.css';
import logo from './logo_login.png'
import 'bootstrap/dist/css/bootstrap.min.css';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import { AiOutlineUser, AiOutlineKey } from "react-icons/ai";

function Login() {
  return (
    <div className="App">
      <div className="Login">
        <div className="flex_login">
          <img src={logo} className='logoLogin'/>
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
                    />
                  </InputGroup>
                </Form.Group>
                <Button className='buttonLogin'>
                  Đăng nhập
                </Button>
              </Form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
