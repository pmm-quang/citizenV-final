import 'bootstrap/dist/css/bootstrap.min.css';
import Form from 'react-bootstrap/Form';
import './NavbarPage.css'
import logo from './logo_account.png'
import {AiFillCaretDown} from "react-icons/ai";

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
        <div className='option'>Thông tin dân cư</div>
        <div className='option'>Thống kê</div>
        <div className='option'>Tìm kiếm người dân</div>
      </div>
    </div>
  );
}

export default NavbarPage;