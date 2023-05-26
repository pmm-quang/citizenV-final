import "../css/Account.css";
import "bootstrap/dist/css/bootstrap.min.css";
import NavbarPage from "../../../Navbar/NavbarPage.js";
import Button from "react-bootstrap/Button";
import { useState, useEffect } from "react";

function CountDownDate() {

    const [countDownDay, setCountDownDay] = useState()
    const [countDownHour, setCountDownHour] = useState()
    const [countDownMinute, setCountDownMinute] = useState()
    const [countDownSecond, setCountDownSecond] = useState()

    const user_account = JSON.parse(localStorage.getItem("user"));
    const user = user_account.username;
    const role = user_account.role;
    const declarationStatus = user_account.declarationStatus;

    const countDownDate = new Date(user_account.declarationEndTime).getTime();

    const [countDown, setCountDown] = useState(
        countDownDate - new Date().getTime()
    );

    useEffect(() => {
        const interval = setInterval(() => {
            setCountDown(countDownDate - new Date().getTime());
            if (countDown < 0) {
                user_account.declarationStatus = "Đã quá hạn"
                localStorage.setItem("user", JSON.stringify(user_account))
            }
        }, 1000);

        const days = Math.floor(countDown / (1000 * 60 * 60 * 24));
        if (days >= 0 && days <= 9) setCountDownDay("0" + String(days))
        else if (days < 0) setCountDownDay(Math.abs(days) - 1)
        else setCountDownDay(days)
        const hours = Math.floor(
            (countDown % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        );
        if (hours >= 0 && hours <= 9) setCountDownHour("0" + String(hours))
        else if (hours < 0) {
            if (hours >= -10) setCountDownHour("0" + Math.abs(hours) - 1)
            else setCountDownHour(Math.abs(hours) - 1)
        }
        else setCountDownHour(hours)
        const minutes = Math.floor((countDown % (1000 * 60 * 60)) / (1000 * 60));
        if (minutes >= 10) setCountDownMinute(minutes)
        else if (minutes < 0) {
            if (minutes >= -10) setCountDownMinute("0" + Math.abs(minutes) - 1)
            else setCountDownMinute(Math.abs(minutes) - 1)
        }
        else setCountDownMinute("0" + String(minutes))
        const seconds = Math.floor((countDown % (1000 * 60)) / 1000);
        if (seconds >= 10) setCountDownSecond(seconds)
        else if (seconds < 0) {
            if (seconds >= -10) setCountDownSecond("0" + Math.abs(seconds) - 1)
            else setCountDownSecond(Math.abs(seconds) - 1)
        }
        else setCountDownSecond("0" + String(seconds))

        return () => clearInterval(interval);
    }, [countDown]);

    const ShowCountDown = () => {
        return (
            <div className="countDownDeclaration" style={{ backgroundColor: (user_account.declarationStatus === "Đang khai báo") ? 'yellow' : (user_account.declarationStatus === "Đã quá hạn") ? '#FF6969' : null}}>
                <div className="statusDeclaration">
                    {user_account.declarationStatus}
                </div>
                <div className="timeStamp">
                    {"Thời hạn: " + user_account.declarationEndTime.substring(8, 10) + "/" + user_account.declarationEndTime.substring(5, 7) + "/" + user_account.declarationEndTime.substring(0, 4)}
                    <div>{(user_account.declarationStatus === "Đang khai báo") ? "Thời gian còn lại: " : "Thời gian quá hạn"}</div>
                    <div>{countDownDay + " ngày " + countDownHour + " giờ " + countDownMinute + " phút " + countDownSecond + " giây"}</div>
                </div>
            </div>
        )
    }
    return (
        <div>
            {(user_account.declarationStatus === "Đang khai báo" || (user_account.declarationStatus === "Đã quá hạn")) ? <ShowCountDown /> : null}
        </div>
    );
}

export default CountDownDate;
