import "./Account.css";
import "bootstrap/dist/css/bootstrap.min.css";
import NavbarPage from "../Navbar/NavbarPage.js";
import Button from "react-bootstrap/Button";
import { useState, useEffect } from "react";
import Table from "react-bootstrap/Table";
import axios from "axios";
import Modal from "react-bootstrap/Modal";

function Account() {
  const [selectAll, setSelectAll] = useState(false);
  const [accountList, setAccountList] = useState([]);
  const [premission, setPremission] = useState(false)

  const role_acc = JSON.parse(localStorage.getItem("user"));
  const user = role_acc.username;

  const GetAllAccount = async () => {
    const response = await axios.get("http://localhost:8080/api/v1/user/")
    setAccountList(response.data)
  }

  useEffect(() => {
    GetAllAccount()
    console.log(accountList)
  }, [])

  
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

  const tableAccount = accountList.map((account) => (
    (account.username === 'tw1' || (user !== 'tw1' && account.username.substring(0, account.username.length - 2) !== user) || (user === 'tw1' && account.username.length !== 2)) ? null : <tr className="top-row" key={account.username} style={{ backgroundColor: account.checked ? 'yellow' : null }}>
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
      {(account.isActive) ? <th className="top-row-title" style={{ color: 'green' }}>Đang hoạt động</th> : <th className="top-row-title" style={{ color: 'red' }}>Đã khóa</th>}
    </tr>
  ));

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
          <Button className="account-option">Thêm tài khoản</Button>
          <Button className="account-option">Xem lịch sử</Button>
          {(premission) ? <Button className="account-option">Cấp quyền khai báo</Button> : null}
        </div>
      </div>
    </div>
  );
}

export default Account;
