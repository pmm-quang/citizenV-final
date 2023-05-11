import "./Account.css";
import "bootstrap/dist/css/bootstrap.min.css";
import NavbarPage from "../Navbar/NavbarPage.js";
import Button from "react-bootstrap/Button";
import { useState, useEffect } from "react";
import Table from "react-bootstrap/Table";
import axios from "axios";
import Modal from "react-bootstrap/Modal";

function Account() {
  return (
    <div>
      <NavbarPage />
      <div className="account-main-page">
        <div className="account-table">
          <Table striped bordered hover>
            <thead>
              <tr className="top-row">
                <th className="top-row-checkbox">
                  <input type="checkbox"></input>
                </th>
                <th className="top-row-title">Mã</th>
                <th className="top-row-title">Tỉnh/Thành phố</th>
                <th className="top-row-title">Thời điểm bắt đầu khai báo</th>
                <th className="top-row-title">Thời điểm kết thúc khai báo</th>
                <th className="top-row-title">Trạng thái</th>
              </tr>
            </thead>

            <tbody>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data">01</td>
                <td className="item-row-data">Hà Nội</td>
                <td className="item-row-data">13:25 12/05/2023</td>
                <td className="item-row-data">13:25 21/05/2023</td>
                <td className="item-row-data">Đã hoàn thành</td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data">02</td>
                <td className="item-row-data">Hà Giang</td>
                <td className="item-row-data">13:25 12/05/2023</td>
                <td className="item-row-data">13:25 21/05/2023</td>
                <td className="item-row-data">Đã hoàn thành</td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
              </tr>
              <tr className="item-row">
                <td className="item-row-checkbox">
                  <input type="checkbox"></input>
                </td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
                <td className="item-row-data"></td>
              </tr>
            </tbody>
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
