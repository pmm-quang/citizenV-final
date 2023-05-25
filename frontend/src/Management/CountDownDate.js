import "./Account.css";
import "bootstrap/dist/css/bootstrap.min.css";
import NavbarPage from "../Navbar/NavbarPage.js";
import Button from "react-bootstrap/Button";
import logo from './alarm_clock.png'
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
        }, 1000);

        const days = Math.floor(countDown / (1000 * 60 * 60 * 24));
        if (days >= 0 && days <= 9) setCountDownDay("0" + String(days))
        else setCountDownDay(days)
        const hours = Math.floor(
            (countDown % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        );
        if (hours >= 0 && hours <= 9) setCountDownHour("0" + String(hours))
        else setCountDownHour(hours)
        const minutes = Math.floor((countDown % (1000 * 60 * 60)) / (1000 * 60));
        if (minutes >= 10) setCountDownMinute(minutes)
        else setCountDownMinute("0" + String(minutes))
        const seconds = Math.floor((countDown % (1000 * 60)) / 1000);
        if (seconds >= 10) setCountDownSecond(seconds)
        else setCountDownSecond("0" + String(seconds))

        return () => clearInterval(interval);
    }, [countDown]);

    const ShowCountDown = () => {
        return (
            <div className="countDownDeclaration">
                <div className="statusDeclaration" style={{ color: (user_account.declarationStatus === "Đang khai báo") ? "black" : "red" }}>
                    {user_account.declarationStatus}
                </div>
                <div className="timeStamp">
                    {"Thời hạn: " + user_account.declarationEndTime.substring(8, 10) + "/" + user_account.declarationEndTime.substring(5, 7) + "/" + user_account.declarationEndTime.substring(0, 4)}
                    <div>Thời gian còn lại: </div>
                    <div>{countDownDay + " ngày " + countDownHour + " giờ " + countDownMinute + " phút " + countDownSecond + " giây"}</div>
                </div>
            </div>
        )
    }
    return (
        <div>
            {(user_account.declarationStatus === "Đang khai báo") ? <ShowCountDown /> : null}
        </div>
    );
}

export default CountDownDate;
