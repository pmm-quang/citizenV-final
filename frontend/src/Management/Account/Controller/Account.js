import "../css/Account.css";
import "bootstrap/dist/css/bootstrap.min.css";
import NavbarPage from "../../../Navbar/NavbarPage.js";
import Button from "react-bootstrap/Button";
import { useState, useEffect } from "react";
import Table from "react-bootstrap/Table";
import axios from "axios";
import Modal from "react-bootstrap/Modal";
import { Form } from "react-bootstrap";
import { BiCheckCircle } from 'react-icons/bi'
import CountDownDate from "./CountDownDate";

function Account() {
  const user_account = JSON.parse(localStorage.getItem("user"));
  const user = user_account.username;
  const role = user_account.role;

  const [checkedId, setCheckedId] = useState(-1)
  const [checkedRepass, setCheckedRepass] = useState(false)
  const [selectAll, setSelectAll] = useState(false);
  const [accountList, setAccountList] = useState([]);
  const [premission, setPremission] = useState(false)
  const [show, setShow] = useState(false)
  const [showDeclaration, setShowDeclaration] = useState(false)
  const [showCreateDeclaration, setShowCreateDeclaration] = useState(false)
  const [declarations, setDeclarations] = useState([])
  const [idAccount, setIdAccount] = useState()
  const [nameDivision, setNameDivison] = useState([])
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
  const [status, setStatus] = useState(user_account.declarationStatus)
  const [codeBlockDeclaration, setCodeBlockDeclaration] = useState()
  const [message, setMessage] = useState()
  const [showWarning, setShowWarning] = useState(false)
  const [showChangePassword, setShowChangePassword] = useState(false)
  const [idAccountChangePassword, setIdAccountChangePassword] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [repeatNewPassword, setRepeatNewPassword] = useState('')

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
        const response = await axios('http://localhost:8080/api/v1/province/', config);
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
      declaration: null,
      division: {
        code: userAccount,
        name: "",
        administrativeUnit: {
        }
      }
    }

    if (role === 'A1') {
      const response = await axios('http://localhost:8080/api/v1/province/' + userAccount, config);
      account.division.name = response.data.name
      account.division.administrativeUnit = response.data.administrativeUnit
    } else if (role === 'A2') {
      const response = await axios('http://localhost:8080/api/v1/district/' + userAccount, config);
      account.division.name = response.data.name
      account.division.administrativeUnit = response.data.administrativeUnit
    } else if (role === 'A3') {
      const response = await axios('http://localhost:8080/api/v1/ward/' + userAccount, config);
      account.division.name = response.data.name
      account.division.administrativeUnit = response.data.administrativeUnit
    } else if (role === 'B1') {
      const response = await axios('http://localhost:8080/api/v1/hamlet/' + userAccount, config);
      account.division.name = response.data.name
      account.division.administrativeUnit = response.data.administrativeUnit
    }

    console.log(account)
    if (checkedId !== 2 && password === repeatPassword) {
      try {
        await axios.post("http://localhost:8080/api/v1/user/save", account, config)
        setShow(false)
        const response = await axios('http://localhost:8080/api/v1/user/', config);
        setAccountList(response.data);
        console.log(response.data)
        setShowWarning(false)
      } catch (error) {
        let messageEdit = String(error.response.data)
        if (messageEdit.toUpperCase() === "ACCESS IS DENIED") setMessage("TÀI KHOẢN HIỆN KHÔNG CÓ QUYỀN THỰC HIỆN")
        else setMessage(messageEdit.toUpperCase())
        setShowWarning(false)
        setShowWarning(true)
      }
    } else if (password !== repeatPassword) {
      setCheckedRepass(true)
    }
  }

  useEffect(() => {
    GetAllAccount()
    fetchFullDetail();
    fetchFullDeclaration()
    console.log(nowTime)
  }, [])

  const listAccDeclaration = accountList.map((account, index) =>
    (account.declaration.startTime === null || account.declaration.status === "Đã hoàn thành" || account.declaration.status === "Đã khóa" || account.declaration.status === "Đã quá hạn khai báo") ? <option key={index} value={account.username}>{account.username + ". " + account.division.administrativeUnit.shortName + " " + account.division.name}</option> : null
  )

  const listAccAcceptDeclaration = accountList.map((account, index) =>
    (account.declaration.status === "Đang khai báo" || account.declaration.status === "Chưa mở khai báo") ? <option key={index} value={account.username}>{account.username + ". " + account.division.administrativeUnit.shortName + " " + account.division.name}</option> : null
  )

  const listDivision = division.map((post) =>
    <option key={post.code} value={post.code}>{post.code + ". " + post.administrativeUnit.shortName + " " + post.name}</option>
  );

  const listDeclaration = declarations.map((declaration, index) =>
    <tr key={index} className="rowTable" style={{ backgroundColor: (CheckedTimeDeclaration(declaration.endTime)) ? "lime" : "red" }}>
      <td>{declaration.startTime}</td>
      <td>{declaration.endTime}</td>
      <td>{CheckedTimeDeclaration(declaration.endTime) ? "Đang khai báo" : "Đã hết hạn"}</td>
    </tr>
  )

  const CheckedIdNewAccount = async (id) => {
    setCheckedId(0)
    let count = 0;
    for (let i = 0; i < accountList.length; i++) {
      if (accountList[i].username === id) {
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

  const BlockAccountDeclaration = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/v1/declaration/lock-declaration/" + codeBlockDeclaration, config)
      console.log(response.data)
      GetAllAccount()
      setShowDeclaration(false)
    } catch (error) {
      console.log(error)
    }
  };

  const CreateAccount = () => {
    setShow(true)
    setCheckedId(0)
    setCheckedRepass(false)
    setUserAccount()
    setPassword()
    setRepeatPassword()
  }

  const tableAccount = accountList.map((account) => (
    <tr className="top-row" key={account.username} style={{ backgroundColor: (account.declaration.status === "Đang khai báo") ? 'yellow' : (account.declaration.status === "Đã hoàn thành") ? "rgb(108, 238, 108)" : (account.declaration.status === "Đã khóa") ? "#FF6969" : null }}>
      <th className="top-row-title">{account.username}</th>
      <th className="top-row-title">{account.division.administrativeUnit.shortName + " " + account.division.name}</th>
      {(account.declaration.startTime === null) ? <th className="top-row-title">Chưa khai báo</th> : <th className="top-row-title">{account.declaration.startTime}</th>}
      {(account.declaration.endTime === null) ? <th className="top-row-title">Chưa khai báo</th> : <th className="top-row-title">{account.declaration.endTime}</th>}
      {<th className="top-row-title">{account.declaration.status}</th>}
    </tr>
  ));

  const listAccountChangePassword = accountList.map((account) => (
    <option value={account.username}>{account.username + ". " + account.division.name}</option>
  ));

  const CompleteDeclaration = async () => {
    try {
      await axios.get("http://localhost:8080/api/v1/declaration/set-completed", config)
      setStatus("Đã hoàn thành")
      user_account.declarationStatus = "Đã hoàn thành"
      localStorage.setItem("user", JSON.stringify(user_account))
      GetAllAccount()
    } catch (error) {
      console.log(error)
    }
  }

  const AddDeclaration = async () => {
    const startDate = new Date(createStartTime)
    const endDate = new Date(createEndTime)
    const declarationEndTime = new Date(user_account.declarationEndTime)
    const timeNow = new Date()
    const declaration = {
      startTime: createStartTime,
      endTime: createEndTime,
      status: "Đang khai báo"
    }
    console.log(declaration)
    console.log(createEndTime)
    console.log(declarationEndTime < endDate)
    if (startDate > endDate) {
      setShowWarning(true)
      setMessage("THỜI GIAN KẾT THÚC KHAI BÁO ĐANG NHỎ HƠN THỜI GIAN BẮT ĐẦU KHAI BÁO")
    } else if (endDate <= timeNow) {
      setShowWarning(true)
      setMessage("THỜI GIAN KẾT THÚC KHAI BÁO ĐANG NHỎ HƠN THỜI GIAN HIỆN TẠI")
    } else if ((declarationEndTime < endDate) && user_account.role !== 'A1') {
      setShowWarning(true)
      setMessage("THỜI GIAN KẾT THÚC KHAI BÁO Ở CẤP DƯỚI PHẢI NHỎ HƠN CẤP TRÊN")
    } else {
      try {
        await axios.put("http://localhost:8080/api/v1/declaration/save/" + idAccount, declaration, config);
        setShowCreateDeclaration(false)
        GetAllAccount()
      } catch (error) {
        console.log(error)
      }
    }
  }

  const ChangePassword = async () => {
    if (newPassword === '' || repeatNewPassword === '' || idAccountChangePassword === '') {
      setShowWarning(true)
      setMessage("HÃY NHẬP ĐẦY ĐỦ THÔNG TIN TRƯỚC KHI XÁC NHẬN THAY ĐỔI")
    } else if (newPassword !== repeatNewPassword) {
      setShowWarning(true)
      setMessage("MẬT KHẨU NHẬP LẠI KHÔNG TRÙNG KHỚP VỚI MẬT KHẨU TRƯỚC ĐÓ")
    } else {
      console.log(newPassword)
      try {
        await axios.put("http://localhost:8080/api/v1/user/change-password/" + idAccountChangePassword, newPassword, config)
        setShowChangePassword(false)
      } catch (error) {
        console.log(error)
        setShowWarning(true)
        let messageEdit = String(error.response.data)
        if (error.response.status === 403) setMessage("TÀI KHOẢN HIỆN KHÔNG CÓ QUYỀN THỰC HIỆN")
        else setMessage(messageEdit.toUpperCase())
      }
    }
  }

  const ClickButtonChangePassword = () => {
    setShowChangePassword(true);
    setIdAccountChangePassword('')
    setNewPassword('')
    setRepeatNewPassword('')
    setShowWarning(false)
  }
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
                CheckedIdNewAccount(e.target.value)
              }
              }>
                <option>
                </option>
                {listDivision}
              </Form.Select>
            </Form.Group>
            {(checkedId === 2) ? <div className="warningChecked">Tài khoản của đơn vị hành chính được chọn đã tồn tại</div> : null}
            <Form.Group className="mb-3">
              <Form.Label>Tên tài khoản (*)</Form.Label>
              <Form.Control value={userAccount} disabled />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Mật khẩu (*)</Form.Label>
              <Form.Control type="password" value={password} onChange={(e) => {
                setPassword(e.target.value)
                setCheckedRepass(false)
              }
              } />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Nhập lại mật khẩu (*)</Form.Label>
              <Form.Control type="password" value={repeatPassword} onChange={(e) => {
                setRepeatPassword(e.target.value)
                setCheckedRepass(false)
              }} />
            </Form.Group>
            {(checkedRepass) ? <div className="warningChecked">Mật khẩu không trùng với mật khẩu được nhập trước đó</div> : null}
          </Form>
          {(showWarning) ? <div className="noteWarning"><p>{message}</p></div> : null}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => {
            setShow(false)
            setShowWarning(false)
          }}>
            Đóng
          </Button>
          <Button variant="primary" onClick={() => {
            CreateNewAccount()
          }}>
            Xác nhận
          </Button>
        </Modal.Footer>
      </Modal>
    )
  }

  const ModalListDeclaration = () => {
    return (
      <Modal show={showDeclaration}>
        <Modal.Header className='headerModal'>
          <Modal.Title className='titleModal'>KHÓA QUYỀN KHAI BÁO</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Label>Chọn đơn vị hành chính cấp dưới</Form.Label>
            <Form.Select value={codeBlockDeclaration} onChange={(e) => setCodeBlockDeclaration(e.target.value)}>
              <option></option>
              {listAccAcceptDeclaration}
            </Form.Select>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => {
            setShowDeclaration(false)
          }}>
            Đóng
          </Button>
          <Button variant="secondary" onClick={() => {
            BlockAccountDeclaration()
          }}>
            Xác nhận
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
          {(showWarning) ? <div className="noteWarning"><p>{message}</p></div> : null}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => {
            setShowCreateDeclaration(false)
            setShowWarning(false)
          }}>
            Đóng
          </Button>
          <Button variant="secondary" onClick={() => {
            AddDeclaration()
          }}>
            Lưu
          </Button>
        </Modal.Footer>
      </Modal>
    )
  }

  const ModalChangePassword = () => {
    return (
      <Modal show={showChangePassword}>
        <Modal.Header className='headerModal'>
          <Modal.Title className='titleModal'>CẤP MỚI MẬT KHẨU CHO TÀI KHOẢN CẤP DƯỚI</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Chọn đơn vị hành chính (*)</Form.Label>
              <Form.Select value={idAccountChangePassword} onChange={(e) => {
                setIdAccountChangePassword(e.target.value)
                setShowWarning(false)
              }
              }>
                <option>
                </option>
                {listAccountChangePassword}
              </Form.Select>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Mật khẩu mới</Form.Label>
              <Form.Control type="password" value={newPassword} onChange={(e) => {
                setNewPassword(e.target.value)
                setShowWarning(false)
              }} />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Nhập lại mật khẩu</Form.Label>
              <Form.Control type="password" value={repeatNewPassword} onChange={(e) => {
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
            setShowWarning(false)
          }}>
            Đóng
          </Button>
          <Button variant="secondary" onClick={() => {
            ChangePassword()
          }}>
            Lưu
          </Button>
        </Modal.Footer>
      </Modal>
    )
  }

  const UpdateCompleteDeclaration = () => {
    return (
      <div className="countDownDeclaration" style={{ height: '100px' }}>
        <div className="statusDeclaration" style={{ marginTop: '20px' }}>
          {status}
        </div>
      </div>
    )
  }

  const UpdateBlockDeclaration = () => {
    return (
      <div className="countDownDeclaration" style={{ height: '100px', backgroundColor: '#FF6969' }}>
        <div className="statusDeclaration" style={{ marginTop: '20px' }}>
          {status}
        </div>
      </div>
    )
  }

  return (
    <div>
      <NavbarPage />
      <div className="account-main-page">
        <div className="account-option-list">
          <Button className="account-option" onClick={() => CreateAccount()}>Thêm tài khoản</Button>
          {((role === 'A1' || status === "Đang khai báo") && (role !== 'B2')) ? <Button className="account-option" onClick={() => setShowCreateDeclaration(true)}>Cấp quyền khai báo</Button> : null}
          {(role === 'B1' && status === "Đang khai báo") ? <Button className="account-option" onClick={() => CompleteDeclaration()}>Hoàn thành khai báo</Button> : null}
          {(status === "Đã khóa") ? null : <Button className="account-option" onClick={() => setShowDeclaration(true)}>Khóa khai báo</Button>}
          <Button className="account-option" onClick={() => ClickButtonChangePassword()}>Cấp mới mật khẩu</Button>
        </div>
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
        {(status === "Đã hoàn thành") ? <UpdateCompleteDeclaration /> : null}
        {(status === "Đang khai báo") ? <CountDownDate /> : null}
        {(status === "Đã khóa") ? <UpdateBlockDeclaration /> : null}
        {(show) ? ModalAddAccount() : null}
        {(showDeclaration) ? ModalListDeclaration() : null}
        {ModalDeclaration()}
        {ModalChangePassword()}
      </div>
    </div>
  );
}

export default Account;
