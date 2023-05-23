import "./Account.css";
import "bootstrap/dist/css/bootstrap.min.css";
import NavbarPage from "../Navbar/NavbarPage.js";
import Button from "react-bootstrap/Button";
import { useState, useEffect } from "react";
import Table from "react-bootstrap/Table";
import axios from "axios";
import Modal from "react-bootstrap/Modal";
import { Form } from "react-bootstrap";

function Account() {
  const [selectAll, setSelectAll] = useState(false);
  const [accountList, setAccountList] = useState([]);
  const [premission, setPremission] = useState(false)
  const [show, setShow] = useState(false)
  const [provinces, setProvinces] = useState([])
  const [districts, setDistricts] = useState([])
  const [wards, setWards] = useState([])
  const [hamlets, setHamlets] = useState([])
  const [division, setDivision] = useState([])
  const [divisionAccount, setDivisionAccount] = useState()
  const [userAccount, setUserAccount] = useState([])
  const [password, setPassword] = useState()
  const [repeatPassword, setRepeatPassword] = useState()

  const user_account = JSON.parse(localStorage.getItem("user"));
  const user = user_account.info.username;
  const role = user_account.info.role;
  const config = {
    headers: {
      Authorization: `Bearer ${user_account.accessToken}`
    },
  };

  const fetchFullProvince = async () => {
    try {
      const response = await axios('http://localhost:8080/api/v1/province/', config);
      setDivision(response.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchFullDistrict = async () => {
    try {
      const response = await axios('http://localhost:8080/api/v1/district/by-province/' + user, config);
      setDivision(response.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchFullWard = async () => {
    try {
      const response = await axios('http://localhost:8080/api/v1/ward/by-district/' + user, config);
      setDivision(response.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchFullHamlet = async () => {
    try {
      const response = await axios('http://localhost:8080/api/v1/hamlet/by-ward/' + user, config);
      setDivision(response.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchDetailProvince = async () => {
    try {
      const response = await axios('http://localhost:8080/api/v1/province/'+ userAccount, config);
      setDivisionAccount(response.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchDetailDistrict = async () => {
    try {
      const response = await axios('http://localhost:8080/api/v1/district/' + userAccount, config);
      console.log(response.data)
      setDivisionAccount(response.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchDetailWard = async () => {
    try {
      const response = await axios('http://localhost:8080/api/v1/ward/' + userAccount, config);
      setDivisionAccount(response.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchDetailHamlet = async () => {
    try {
      const response = await axios('http://localhost:8080/api/v1/hamlet/' + userAccount, config);
      setDivisionAccount(response.data);
    } catch (err) {
      console.error(err);
    }
  };

  const GetAllAccount = async () => {
    const response = await axios.get("http://localhost:8080/api/v1/user/", config)
    setAccountList(response.data)
    console.log(response.data)
  }

  const CreateNewAccount = async () => {
    if (role === 'A1') fetchDetailProvince()
    if (role === 'A2') fetchDetailDistrict()
    if (role === 'A3') fetchDetailWard()
    if (role === 'B1') fetchDetailHamlet()
    
    const account = {
      username: userAccount,
      password: password,
      isActive: true,
      roles: null,
      declaration: null,
      division: divisionAccount
    }
    console.log(account)
  }

  useEffect(() => {
    GetAllAccount()
    if (role === 'A1') fetchFullProvince()
    else if (role === 'A2') fetchFullDistrict()
    else if (role === 'A3') fetchFullWard()
    else if (role === 'B1') fetchFullHamlet()
  }, [])

  const listDivision = division.map((post) =>
    <option key={post.code} value={post.code}>{post.code + ". " + post.administrativeUnit.shortName + " " + post.name}</option>
  );

  const checkAccount = (accountList) => {
    const updatedCheckboxes = accountList.map((checkbox) => ({
      ...checkbox,
      checked: false,
    }));
    setAccountList(updatedCheckboxes);
  };

  const handleSelectAll = () => {
    const updatedCheckboxes = accountList.map((checkbox) => ({
      ...checkbox,
      checked: !selectAll,
    }));
    setAccountList(updatedCheckboxes);
    setSelectAll(!selectAll);
  };

  const handleCheckboxChange = (id) => {
    const updatedCheckboxes = accountList.map((checkbox) =>
      checkbox.username === id
        ? { ...checkbox, checked: !checkbox.checked }
        : checkbox
    );
    setAccountList(updatedCheckboxes);
    setSelectAll(updatedCheckboxes.every((checkbox) => checkbox.checked));
  };

  const CreateAccount = () => {
    setShow(true)
  }

  const tableAccount = accountList.map((account) => (
    (account.username === 'tw1') ? null : <tr className="top-row" key={account.username} style={{ backgroundColor: account.checked ? 'yellow' : null }}>
      <th className="top-row-checkbox">
        <input
          type="checkbox"
          id="checkbox-all"
          checked={account.checked}
          onChange={(e) => {
            handleCheckboxChange(account.username)
          }}
        ></input>
      </th>
      <th className="top-row-title">{account.username}</th>
      <th className="top-row-title">{account.division.administrativeUnit.shortName + " " + account.division.name}</th>
      {(account.declaration === null) ? <th className="top-row-title">Chưa khai báo</th> : <th className="top-row-title">{account.declaration.startTime}</th>}
      {(account.declaration === null) ? <th className="top-row-title">Chưa khai báo</th> : <th className="top-row-title">{account.declaration.endTime}</th>}
      {(account.isActive) ? <th className="top-row-title" style={{ color: 'green' }}>Đang khai báo</th> : <th className="top-row-title" style={{ color: 'red' }}>Chưa khai báo</th>}
    </tr>
  ));

  const ModalAddAccount = () => {
    return (
      <Modal show={show}>
        <Modal.Header className='headerModal'>
          <Modal.Title className='titleModal'>THÊM TÀI KHOẢN MỚI</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Tên tỉnh/thành phố (*)</Form.Label>
              <Form.Select value = {userAccount} onChange={(e) => setUserAccount(e.target.value)}>
                <option>
                </option>
                {listDivision}
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Tên tài khoản (*)</Form.Label>
              <Form.Control value = {userAccount} disabled/>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Mật khẩu (*)</Form.Label>
              <Form.Control type = "password" value = {password} onChange={(e) => setPassword(e.target.value)}/>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Nhập lại mật khẩu (*)</Form.Label>
              <Form.Control type = "password" value = {repeatPassword} onChange={(e) => setRepeatPassword(e.target.value)}/>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => {
            setShow(false)
          }}>
            Đóng
          </Button>
          <Button variant="primary" onClick={() => { 
            CreateNewAccount()
          }}>
            Lưu
          </Button>
        </Modal.Footer>
      </Modal>
    )
  }

  return (
    <div>
      <NavbarPage />
      <div className="account-main-page">
        <div className="account-table">
          <Table striped bordered hover>
            <thead>
              <tr className="top-row">
                <th className="top-row-checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-all"
                    checked={selectAll}
                    onChange={handleSelectAll}
                  ></input>
                </th>
                <th className="top-row-title">Mã</th>
                <th className="top-row-title">Đơn vị hành chính</th>
                <th className="top-row-title">Thời điểm bắt đầu khai báo</th>
                <th className="top-row-title">Thời điểm kết thúc khai báo</th>
                <th className="top-row-title">Trạng thái</th>
              </tr>
            </thead>
            <tbody>{tableAccount}</tbody>
          </Table>
        </div>

        <div className="account-option-list">
          <Button className="account-option" onClick={() => CreateAccount()}>Thêm tài khoản</Button>
          <Button className="account-option">Xem lịch sử</Button>
          {(premission) ? <Button className="account-option">Cấp quyền khai báo</Button> : null}
        </div>
        {(show) ? ModalAddAccount() : null}
      </div>
    </div>
  );
}

export default Account;
