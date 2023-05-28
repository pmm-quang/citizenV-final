import "../css/InfoAccount.css";
import "bootstrap/dist/css/bootstrap.min.css";
import NavbarPage from "../../../Navbar/NavbarPage.js";
import Button from "react-bootstrap/Button";
import { useState, useEffect } from "react";
import { Table } from "react-bootstrap";

function InfoAccount() {
    const user_account = JSON.parse(localStorage.getItem("user"));

    const GetInfoAccount = () => {
        return (
            <div>
                <div className="titleAccount">THÔNG TIN TÀI KHOẢN</div>
                <div className="detailed-resident-info">
                    <Table bordered hover className="tableInfo">
                        <tbody>
                            <tr>
                                <th className="row-head">Mã đơn vị</th>
                                <th className="row-data">{user_account.username}</th>
                            </tr>
                            <tr>
                                <th className="row-head">Tên đơn vị hành chính</th>
                                <th className="row-data">{user_account.division.name}</th>
                            </tr>
                            <tr>
                                <th className="row-head">Phân loại đơn vị hành chính</th>
                                <th className="row-data">{user_account.division.administrativeUnit.fullName}</th>
                            </tr>
                            <tr>
                                <th className="row-head">Trạng thái</th>
                                <th className="row-data">{user_account.declarationStatus}</th>
                            </tr>
                            {(user_account.declarationStatus === "Đang khai bao" || user_account.declarationStatus === "Chưa bắt đầu") ?
                                <tr>
                                    <th className="row-head">Thời gian bắt đầu khai báo</th>
                                    <th className="row-data">{user_account.declarationStartTime.substring(8,10) + " tháng " + user_account.declarationStartTime.substring(5,7) + " năm " + user_account.declarationStartTime.substring(0,4)}</th>
                                </tr>
                                : null}
                            {(user_account.declarationStatus === "Đã khóa" || user_account.declarationStatus === "Chưa bắt đầu") ?
                                <tr>
                                    <th className="row-head">Thời gian kết thúc khai báo</th>
                                    <th className="row-data">{user_account.declarationEndTime.substring(8,10) + " tháng " + user_account.declarationEndTime.substring(5,7) + " năm " + user_account.declarationEndTime.substring(0,4)}</th>
                                </tr>
                                : null}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }

    return (
        <div>
            <NavbarPage />
            <div className="pageInfoAccount">
                <div className="optionsList">
                    <div className="optionSelect">
                        <Button className="buttonOption">Xem thông tin tài khoản</Button>
                    </div>
                    <div className="optionSelect">
                        <Button className="buttonOption">Đổi mật khẩu</Button>
                    </div>
                </div>
                <div className="showFlexPage">
                    <GetInfoAccount />
                </div>
            </div>
        </div>
    );
}

export default InfoAccount;
