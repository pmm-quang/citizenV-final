import 'bootstrap/dist/css/bootstrap.min.css';
import Form from 'react-bootstrap/Form';
import './NavbarPage.css'
import logo from './logo_account.png'
import { AiOutlineMenu, AiOutlineLogout } from "react-icons/ai";
import { Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import {BsPersonCircle} from 'react-icons/bs'

function NavbarPage() {
  const role_acc = JSON.parse(localStorage.getItem("user"));
  const author = role_acc.roles[0].authority;

  let navigate = useNavigate();
  return (
    <div>
      <div className="headerPage">
        <img src={logo} className='logoIcon' />
        <p className="nameApp">CitizenV</p>
        <div className='formLogout'>
          <BsPersonCircle className='iconAccount'/>
          {(author === 'A1') ? role_acc.username + " - Trung ương" : null}
          {(author === 'B2') ? role_acc.username + " - " + role_acc.division.administrativeUnit.shortName + " " + role_acc.division.name : null}
          {(author !== 'A1' && author !== 'B2') ? role_acc.username + " - " + role_acc.division.name : null}
          <AiOutlineLogout className='iconLogout' onClick={() => navigate('/login')} />
        </div>

      </div>
      <div className="listOptions">
        {(author === 'A1') ? <div className='option' style={{ marginLeft: '115px' }} onClick={() => { navigate("/province") }}>Quản lý tỉnh, thành phố</div> : null}
        {(author === 'A2') ? <div className='option' style={{ marginLeft: '115px' }} onClick={() => { navigate("/district") }}>Quản lý quận, huyện, thị xã</div> : null}
        {(author === 'A3') ? <div className='option' style={{ marginLeft: '115px' }} onClick={() => { navigate("/district") }}>Quản lý xã, phường, thị trấn</div> : null}
        {(author === 'EDITOR') ? <div className='option' style={{ marginLeft: '115px' }} onClick={() => { navigate("/district") }}>Quản lý tổ dân phố, thôn, xóm</div> : null}
        {(author !== 'B2') ? <div className='option' onClick={() => { navigate("/account") }}>Quản lý tài khoản</div> : null}
        {(author === 'B2') ? <div className='option' style={{ marginLeft: '115px' }} onClick={() => { navigate("/citizen") }}>Thông tin dân cư</div> : null}
        {(author !== 'B2') ? <div className='option' onClick={() => { navigate("/citizen") }}>Thông tin dân cư</div> : null}
        <div className='option' onClick={() => { navigate("/statis") }}>Thống kê</div>
        <div className='option' onClick={() => { navigate("/findresidential") }}>Tìm kiếm người dân</div>
        {(author === 'B2') ? <div className='option' onClick={() => { navigate("/citizeninput") }}>Nhập liệu</div> : null}

        <div className='sub-nav'>
          <AiOutlineMenu className='icon-sub-nav' style={{ height: '60px' }} />
          <div className='sub-option-list'>
            {(author === 'A1') ? <div className='sub-option' onClick={() => { navigate("/province") }}>Quản lý tỉnh, thành phố</div> : null}
            {(author === 'A2') ? <div className='sub-option' onClick={() => { navigate("/district") }}>Quản lý quận, huyện, thị xã</div> : null}
            {(author === 'A3') ? <div className='sub-option' onClick={() => { navigate("/district") }}>Quản lý xã, phường, thị trấn</div> : null}
            {(author === 'EDITOR') ? <div className='sub-option' onClick={() => { navigate("/district") }}>Quản lý tổ dân phố, thôn, xóm</div> : null}
            {(author !== 'B2') ? <div className='sub-option'>Quản lý tài khoản</div> : null}
            <div className='sub-option'>Thông tin dân cư</div>
            <div className='sub-option'>Thống kê</div>
            <div className='sub-option'>Tìm kiếm người dân</div>
            {(author === 'B2') ? <div className='sub-option'>Nhập liệu</div> : null}
          </div>
        </div>
      </div>
    </div>
  );
}

export default NavbarPage;