import "./Account.css";
import "bootstrap/dist/css/bootstrap.min.css";
import NavbarPage from "../Navbar/NavbarPage.js";
import Button from "react-bootstrap/Button";
import { useState, useEffect } from "react";
import Table from "react-bootstrap/Table";
import axios from "axios";
import Modal from "react-bootstrap/Modal";
import { Form } from "react-bootstrap";
import { BiCheckCircle } from 'react-icons/bi'

function Account() {
  const [checkedId, setCheckedId] = useState(-1)
  const [checkedTime, setCheckedTime] = useState(false)
  const [selectAll, setSelectAll] = useState(false);
  const [accountList, setAccountList] = useState([]);
  const [premission, setPremission] = useState(false)
  const [show, setShow] = useState(false)
  const [showDeclaration, setShowDeclaration] = useState(false)
  const [showCreateDeclaration, setShowCreateDeclaration] = useState(false)
  const [declarations, setDeclarations] = useState([])
  const [idAccount, setIdAccount] = useState()
  const [districts, setDistricts] = useState([])
  const [wards, setWards] = useState([])
  const [hamlets, setHamlets] = useState([])
  const [division, setDivision] = useState([])
  const [divisionAccount, setDivisionAccount] = useState({})
  const [userAccount, setUserAccount] = useState([])
  const [password, setPassword] = useState()
  const [repeatPassword, setRepeatPassword] = useState()
  const [nowTime, setNowTime] = useState(Date())
  const [createStartTime, setCreateStartTime] = useState()
  const [createEndTime, setCreateEndTime] = useState()

  const user_account = JSON.parse(localStorage.getItem("user"));
  const user = user_account.info.username;
  const role = user_account.info.role;

  const config = {
    headers: {
      Authorization: `Bearer ${user_account.accessToken}`
    },
  };

  const fetchFullDeclaration = async () => {
    const response = await axios('http://localhost:8080/api/v1/declaration/', config);
    setDeclarations(response.data);
  }

  const fetchFullDetail = async () => {
    try {
      if (role === 'A1') {
        const response = await axios('http://localhost:8080/api/v1/province', config);
        setDivision(response.data);
      } else if (role === 'A2') {
        const response = await axios('http://localhost:8080/api/v1/district/by-province/' + user, config);
        setDivision(response.data);
      } else if (role === 'A3') {
        const response = await axios('http://localhost:8080/api/v1/ward/by-district/' + user, config);
        setDivision(response.data);
      } else if (role === 'B1') {
        const response = await axios('http://localhost:8080/api/v1/hamlet/by-ward/' + user, config);
        setDivision(response.data);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const CheckedTimeDeclaration = (endTime) => {
    const newDate = new Date(endTime)
    if (newDate > Date.now()) return true;
    else {
      return false
    }
  }

  const GetAllAccount = async () => {
    const response = await axios.get("http://localhost:8080/api/v1/user/", config)
    setAccountList(response.data)
    console.log(response.data)
  }

  const CreateNewAccount = async () => {
    const account = {
      username: userAccount,
      password: password,
      isActive: true,
      roles: [],
      declaration: null,
      division: {}
    }

    if (role === 'A1') {
      const response = await axios('http://localhost:8080/api/v1/province/' + userAccount, config);
      account.division = response.data;
    } else if (role === 'A2') {
      const response = await axios('http://localhost:8080/api/v1/district/' + userAccount, config);
      account.division = response.data;
    } else if (role === 'A3') {
      const response = await axios('http://localhost:8080/api/v1/ward/' + userAccount, config);
      account.division = response.data;
    } else if (role === 'B1') {
      const response = await axios('http://localhost:8080/api/v1/hamlet/' + userAccount, config);
      account.division = response.data;
    }

    console.log(account)
    if (checkedId !== 2 && password === repeatPassword) {
      try {
        await axios.post("http://localhost:8080/api/v1/user/save", account, config)
        setShow(false)
        const response = await axios('http://localhost:8080/api/v1/user/', config);
        setAccountList(response.data);
      } catch (err) {
        console.error(err);
      }
    }
  }

  useEffect(() => {
    GetAllAccount()
    fetchFullDetail();
    fetchFullDeclaration()
    console.log(nowTime)
  }, [])

  const listAccDeclaration = accountList.map((account, index) =>
    (account.declaration === null) ? <option key={index} value={account.username}>{account.username + ". " + account.division.name}</option> : null
  )

  const listDivision = division.map((post) =>
    <option key={post.code} value={post.code}>{post.code + ". " + post.administrativeUnit.shortName + " " + post.name}</option>
  );

  const listDeclaration = declarations.map((declaration, index) =>
    <tr key={index} className="rowTable" style={{ backgroundColor: (CheckedTimeDeclaration(declaration.endTime)) ? "lime" : "red" }}>
      <td>{declaration.startTime.substring(0, 10)}</td>
      <td>{declaration.endTime.substring(0, 10)}</td>
      <td>{CheckedTimeDeclaration(declaration.endTime) ? "Đang khai báo" : "Đã hết hạn"}</td>
    </tr>
  )

  const CheckedIdNewAccount = async () => {
    setCheckedId(0)
    let count = 0;
    for (let i = 0; i < accountList.length; i++) {
      if (accountList[i].username === userAccount) {
        count++;
      }
    }
    if (count === 0) setCheckedId(1)
    else setCheckedId(2)
  }

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
    <tr className="top-row" key={account.username} style={{ backgroundColor: account.checked ? 'yellow' : null }}>
      <th className="top-row-title">{account.username}</th>
      <th className="top-row-title">{account.division.administrativeUnit.shortName + " " + account.division.name}</th>
      {(account.declaration === null) ? <th className="top-row-title">Chưa khai báo</th> : <th className="top-row-title">{account.declaration.startTime.substring(0, 10)}</th>}
      {(account.declaration === null) ? <th className="top-row-title">Chưa khai báo</th> : <th className="top-row-title">{account.declaration.endTime.substring(0, 10)}</th>}
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
              <Form.Select value={userAccount} onChange={(e) => {
                setUserAccount(e.target.value)
                setCheckedId(0)
              }
              }>
                <option>
                </option>
                {listDivision}
              </Form.Select>
            </Form.Group>
            {(checkedId === 0) ? <div className='checked' onClick={() => CheckedIdNewAccount()}><BiCheckCircle className="iconChecked" />Kiểm tra</div> : null}
            {(checkedId === 2) ? <div className="warningChecked">Mã của đơn vị hành chính vừa nhập đang bị trùng với một đơn vị hành chính đã có sẵn</div> : null}
            {(checkedId === 3) ? <div className="warningChecked">Mã của đơn vị hành chính vừa nhập không nằm trong khu vực quản lý</div> : null}
            {(checkedId === 1) ? <div className="successChecked">Bạn có thể sử dụng mã vừa nhập</div> : null}
            <Form.Group className="mb-3">
              <Form.Label>Tên tài khoản (*)</Form.Label>
              <Form.Control value={userAccount} disabled />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Mật khẩu (*)</Form.Label>
              <Form.Control type="password" value={password} onChange={(e) => {
                setPassword(e.target.value)
              }
              } />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Nhập lại mật khẩu (*)</Form.Label>
              <Form.Control type="password" value={repeatPassword} onChange={(e) => {
                setRepeatPassword(e.target.value)
              }} />
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

  const ModalListDeclaration = () => {
    return (
      <Modal show={showDeclaration} size="lg">
        <Modal.Header className='headerModal'>
          <Modal.Title className='titleModal'>THỐNG KÊ CÁC LẦN KHAI BÁO ĐANG TIẾN HÀNH HOẶC ĐÃ QUA</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Table striped bordered hover size="sm" className="tableAccount">
            <thead>
              <tr>
                <th>Ngày bắt đầu</th>
                <th>Ngày kết thúc</th>
                <th>Trạng thái</th>
              </tr>
            </thead>
            <tbody>
              {listDeclaration}
            </tbody>
          </Table>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => {
            setShowDeclaration(false)
          }}>
            Đóng
          </Button>
        </Modal.Footer>
      </Modal>
    )
  }

  const ModalDeclaration = () => {
    return (
      <Modal show={showCreateDeclaration}>
        <Modal.Header className='headerModal'>
          <Modal.Title className='titleModal'>CẤP QUYỀN KHAI BÁO</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Tên đơn vị hành chính (*)</Form.Label>
              <Form.Select value={idAccount} onChange={(e) => {
                setIdAccount(e.target.value)
              }
              }>
                <option>
                </option>
                {listAccDeclaration}
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Ngày bắt đầu (*)</Form.Label>
              <Form.Control type="date" value={createStartTime} onChange={(e) => {
                setCreateStartTime(e.target.value)
              }} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Ngày kết thúc (*)</Form.Label>
              <Form.Control type="date" value={createEndTime} onChange={(e) => {
                setCreateEndTime(e.target.value)
              }}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => {
            setShowCreateDeclaration(false)
          }}>
            Đóng
          </Button>
          <Button variant="secondary" onClick={() => {
            const startDate = new Date(createStartTime)
            const endDate = new Date(createEndTime)
            if (startDate > endDate) console.log('error')
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
              <th className="top-row-title">Mã</th>
              <th className="top-row-title">Đơn vị hành chính</th>
              <th className="top-row-title">Thời điểm bắt đầu khai báo</th>
              <th className="top-row-title">Thời điểm kết thúc khai báo</th>
              <th className="top-row-title">Trạng thái</th>
            </thead>
            <tbody>{tableAccount}</tbody>
          </Table>
        </div>

        <div className="account-option-list">
          <Button className="account-option" onClick={() => CreateAccount()}>Thêm tài khoản</Button>
          <Button className="account-option" onClick={() => setShowDeclaration(true)}>Xem lịch sử</Button>
          <Button className="account-option" onClick={() => setShowCreateDeclaration(true)}>Cấp quyền khai báo</Button>
        </div>
        {(show) ? ModalAddAccount() : null}
        {(showDeclaration) ? ModalListDeclaration() : null}
        {ModalDeclaration()}
      </div>
    </div>
  );
}

export default Account;
