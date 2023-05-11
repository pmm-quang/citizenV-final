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
  const [accountList, setAccountList] = useState([
    {
      id: 1,
      province: "Hà Nội",
      begin: "12/05/2023",
      end: "22/05/2023",
      status: "Chưa khai báo",
    },
    {
      id: 2,
      province: "Hà Giang",
      begin: "12/05/2023",
      end: "22/05/2023",
      status: "Chưa khai báo",
    },
  ]);

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
      checkbox.id === id
        ? { ...checkbox, checked: !checkbox.checked }
        : checkbox
    );
    setAccountList(updatedCheckboxes);
    setSelectAll(updatedCheckboxes.every((checkbox) => checkbox.checked));
  };

  const tableAccount = accountList.map((account) => (
    <tr className="top-row" key={account.id} style = {{backgroundColor: account.checked ? 'yellow' : null}}>
      <th className="top-row-checkbox">
        <input
          type="checkbox"
          id="checkbox-all"
          checked={account.checked}
          onChange={() => handleCheckboxChange(account.id)}
        ></input>
      </th>
      <th className="top-row-title">{account.id}</th>
      <th className="top-row-title">{account.province}</th>
      <th className="top-row-title">{account.begin}</th>
      <th className="top-row-title">{account.end}</th>
      <th className="top-row-title">{account.status}</th>
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
                <th className="top-row-title">Tỉnh/Thành phố</th>
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
          <Button className="account-option">Cấp quyền khai báo</Button>
        </div>
      </div>
    </div>
  );
}

export default Account;
