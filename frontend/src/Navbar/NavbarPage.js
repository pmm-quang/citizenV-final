import 'bootstrap/dist/css/bootstrap.min.css';
import Form from 'react-bootstrap/Form';
import './NavbarPage.css'
import logo from './logo_account.png'
import {AiFillCaretDown, AiOutlineMenu} from "react-icons/ai";
import { Button } from 'react-bootstrap';

function NavbarPage() {
  return (
    <div>
      <div className="headerPage">
        <img src={logo} className='logoIcon' />
        <p className="nameApp">CitizenV</p>
        <div className='formLogout'>
          A1_User
          <AiFillCaretDown className='iconLogout'/>
        </div>

      </div>
      <div className="listOptions">
        <div className='option' style={{ marginLeft: '115px' }}>Quản lý tỉnh/thành phố</div>
        <div className='option'>Quản lý tài khoản</div>
        <div className='option'>Thông tin dân cư</div>
        <div className='option'>Thống kê</div>
        <div className='option'>Tìm kiếm người dân</div>
        <div className='option'>Nhập liệu</div>
        
        <div className='sub-nav'>
          <AiOutlineMenu className='icon-sub-nav'  style={{height: '60px'}}/>
          <div className='sub-option-list'>
            <div className='sub-option'>Quản lý tỉnh/thành phố</div>
            <div className='sub-option'>Quản lý tài khoản</div>
            <div className='sub-option'>Thông tin dân cư</div>
            <div className='sub-option'>Thống kê</div>
            <div className='sub-option'>Tìm kiếm người dân</div>
            <div className='sub-option'>Nhập liệu</div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default NavbarPage;