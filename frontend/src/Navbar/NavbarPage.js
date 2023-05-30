import 'bootstrap/dist/css/bootstrap.min.css';
import './NavbarPage.css'
import logo from './file/logo_color.png'
import { AiOutlineMenu, AiOutlineLogout } from "react-icons/ai";
import { useNavigate } from 'react-router-dom';
import { BsPersonCircle } from 'react-icons/bs'
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Form, Modal, Button } from 'react-bootstrap';
import { useState } from 'react';
import { IoMdConstruct } from 'react-icons/io'
import axios from 'axios';

function NavbarPage() {
  const user_account = JSON.parse(localStorage.getItem("user"));
  const author = user_account.role;

  const [show, setShow] = useState(false)
  const [showChangePassword, setShowChangePassword] = useState(false)
  const [oldPassword, setOldPassword] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [repeatNewPassword, setRepeatNewPassword] = useState('')
  const [message, setMessage] = useState()
  const [showWarning, setShowWarning] = useState(false)

  let navigate = useNavigate();
  const config = {
    headers: {
      Authorization: `Bearer ${user_account.accessToken}`
    },
  };

  const ChangeUserPassword = async () => {
    if (newPassword === '' || repeatNewPassword === '' || oldPassword === '') {
      setMessage("HÃY NHẬP ĐẦY ĐỦ THÔNG TIN TRƯỚC KHI XÁC NHẬN THAY ĐỔI")
      setShowWarning(true)
    } else if (newPassword !== repeatNewPassword) {
      setMessage("MẬT KHẨU NHẬP LẠI KHÔNG TRÙNG KHỚP VỚI MẬT KHẨU TRƯỚC ĐÓ")
      setShowWarning(true)
    } else {
      try {
        await axios.put("http://localhost:8080/api/v1/user/change-password", {
          oldPassword: oldPassword,
          newPassword: newPassword
        }, config)
        setShowWarning(false)
        setShowChangePassword(false)
      } catch (error) {
        console.log(error)
        let messageEdit = String(error.response.data)
        setMessage(messageEdit.toUpperCase())
        setShowWarning(true)
      }
    }
  }

  const GetChangePassWordAccount = () => {
    return (
      <Modal show={showChangePassword}>
        <Modal.Header className='headerModal'>
          <Modal.Title className='titleModal'>THAY ĐỔI MẬT KHẨU</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Mã đơn vị</Form.Label>
              <Form.Control value={user_account.username} disabled />
            </Form.Group>
            {(author === 'A1') ? <Form.Group className="mb-3">
              <Form.Label>Tên đơn vị</Form.Label>
              <Form.Control value={"Trung ương"} disabled />
            </Form.Group> : null}
            {(author !== 'A1') ? <Form.Group className="mb-3">
              <Form.Label>Tên đơn vị</Form.Label>
              <Form.Control value={user_account.division.name} disabled />
            </Form.Group> : null}
            {(author !== 'A1') ? <Form.Group className="mb-3">
              <Form.Label>Phân loại đơn vị hành chính</Form.Label>
              <Form.Control value={user_account.division.administrativeUnit.fullName} disabled />
            </Form.Group> : null}
            {(author !== 'A1') ? <Form.Group className="mb-3">
              <Form.Label>Trạng thái</Form.Label>
              <Form.Control value={user_account.declarationStatus} disabled />
            </Form.Group> : null}
            <Form.Group className="mb-3">
              <Form.Label>Mật khẩu hiện tại</Form.Label>
              <Form.Control type = "password" value={oldPassword} onChange={(e) => {
                setOldPassword(e.target.value)
                setShowWarning(false)
              }} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Mật khẩu mới</Form.Label>
              <Form.Control type = "password" value={newPassword} onChange={(e) => {
                setNewPassword(e.target.value)
                setShowWarning(false)
              }} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Nhập lại mật khẩu</Form.Label>
              <Form.Control type = "password" value={repeatNewPassword} onChange={(e) => {
                setRepeatNewPassword(e.target.value)
                setShowWarning(false)
              }} />
            </Form.Group>
          </Form>
          {(showWarning) ? <div className="noteWarning"><p>{message}</p></div> : null}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => {
            setShowChangePassword(false)
          }}>
            Đóng
          </Button>
          <Button variant="primary" onClick={() => {
            ChangeUserPassword()
          }}>
            Xác nhận
          </Button>
        </Modal.Footer>
      </Modal>
    )
  }

  return (
    <div>
      <div className="headerPage">
        <img src={logo} className='logoIcon' onClick={() => navigate("/home")} />
        <p className="nameApp">CitizenV</p>
        <div className='formLogout'>
          <BsPersonCircle className='iconAccount' />
          {(author === 'A1') ? user_account.username + " - Trung ương" : null}
          {(author === 'B2') ? user_account.username + " - " + user_account.division.administrativeUnit.shortName + " " + user_account.division.name : null}
          {(author !== 'A1' && author !== 'B2') ? user_account.username + " - " + user_account.division.name : null}
          <AiOutlineLogout className='iconLogout' onClick={() => {
            navigate('/login')
            localStorage.clear()
          }
          } />
          <IoMdConstruct className='iconChangePassword' onClick={() => {
            setShowChangePassword(true)
            setOldPassword('')
            setNewPassword('')
            setRepeatNewPassword('')
          }
          } />
        </div>
      </div>
      <div className="listOptions">
        {(author === 'A1') ? <div className='option' style={{ marginLeft: '115px' }} onClick={() => { navigate("/province") }}>Quản lý tỉnh, thành phố</div> : null}
        {(author === 'A2') ? <div className='option' style={{ marginLeft: '115px' }} onClick={() => { navigate("/district") }}>Quản lý quận, huyện, thị xã</div> : null}
        {(author === 'A3') ? <div className='option' style={{ marginLeft: '115px' }} onClick={() => { navigate("/ward") }}>Quản lý xã, phường, thị trấn</div> : null}
        {(author === 'B1') ? <div className='option' style={{ marginLeft: '115px' }} onClick={() => { navigate("/hamlet") }}>Quản lý tổ dân phố, thôn, xóm</div> : null}
        {(author !== 'B2') ? <div className='option' onClick={() => { navigate("/account") }}>Quản lý tài khoản</div> : null}
        {(author === 'B2') ? <div className='option' style={{ marginLeft: '115px' }} onClick={() => { navigate("/citizen") }}>Thông tin dân cư</div> : null}
        {(author !== 'B2') ? <div className='option' onClick={() => { navigate("/citizen") }}>Thông tin dân cư</div> : null}
        <div className='option' onClick={() => { navigate("/statis") }}>Thống kê</div>
        <div className='option' onClick={() => { navigate("/findcitizen") }}>Tìm kiếm người dân</div>
        {(author === 'B2') ? <div className='option' onClick={() => { navigate("/citizeninput") }}>Nhập liệu</div> : null}

        <div className='sub-nav'>
          <AiOutlineMenu className='icon-sub-nav' style={{ height: '60px' }} />
          <div className='sub-option-list'>
            {(author === 'A1') ? <div className='sub-option' onClick={() => { navigate("/province") }}>Quản lý tỉnh, thành phố</div> : null}
            {(author === 'A2') ? <div className='sub-option' onClick={() => { navigate("/district") }}>Quản lý quận, huyện, thị xã</div> : null}
            {(author === 'A3') ? <div className='sub-option' onClick={() => { navigate("/ward") }}>Quản lý xã, phường, thị trấn</div> : null}
            {(author === 'EDITOR') ? <div className='sub-option' onClick={() => { navigate("/hamlet") }}>Quản lý tổ dân phố, thôn, xóm</div> : null}
            {(author !== 'B2') ? <div className='sub-option' onClick={() => { navigate("/account") }}>Quản lý tài khoản</div> : null}
            <div className='sub-option' onClick={() => { navigate("/citizen") }}>Thông tin dân cư</div>
            <div className='sub-option' onClick={() => { navigate("/statis") }}>Thống kê</div>
            <div className='sub-option' onClick={() => { navigate("/findcitizen") }}>Tìm kiếm người dân</div>
            {(author === 'B2') ? <div className='sub-option' onClick={() => { navigate("/citizeninput") }}>Nhập liệu</div> : null}
          </div>
        </div>
      </div>
      {GetChangePassWordAccount()}
    </div>
  );
}

export default NavbarPage;