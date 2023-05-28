import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../../../Navbar/NavbarPage.js';
import Button from 'react-bootstrap/Button'
import '../css/province.css'
import { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import Modal from 'react-bootstrap/Modal';
import { BiCheckCircle } from 'react-icons/bi'

function Ward() {
    const user_account = JSON.parse(localStorage.getItem("user"));
    const user = user_account.username;
    const config = {
        headers: {
            Authorization: `Bearer ${user_account.accessToken}`
        },
    };

    const [showWarning, setShowWarning] = useState(false)
    const [wards, setWards] = useState([]);
    const [show, setShow] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [nameWard, setNameWard] = useState('');
    const [idWard, setIdWard] = useState('');
    const [defaultidWard, setDefaultIdWard] = useState('');
    const [idUnitWard, setIdUnitWard] = useState('');
    const [checkedId, setCheckedId] = useState(-1);
    const [showWarningCreate, setWarningCreate] = useState(false);
    const [message, setMessage] = useState()


    const fetchFullWard = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/ward/by-district/' + user, config);
            setWards(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchDetailWard = async (code) => {
        setShowWarning(false)
        try {
            const response = await axios('http://localhost:8080/api/v1/ward/' + code, config);
            setIdWard(response.data.code)
            setNameWard(response.data.name)
            setIdUnitWard(response.data.administrativeUnit.id)
            setShowEdit(true)
            setDefaultIdWard(response.data.code)
        } catch (err) {
            console.error(err);
        }
    };

    const EditWard = async (code) => {
        const ward = {
            code: idWard,
            name: nameWard,
            districtCode: user,
            administrativeUnitId: idUnitWard
        }
        console.log(ward)
        try {
            await axios.put("http://localhost:8080/api/v1/ward/save/" + defaultidWard + "/", ward, config)
            setShowWarning(false)
            setShowEdit(false)
            const response = await axios('http://localhost:8080/api/v1/ward/by-district/' + user, config);
            setWards(response.data);
        } catch (error) {
            setShowWarning(true)
            let messageEdit = String(error.response.data)
            if (messageEdit.toUpperCase() === "ACCESS IS DENIED") setMessage("TÀI KHOẢN HIỆN KHÔNG CÓ QUYỀN CHỈNH SỬA")
            else setMessage(messageEdit.toUpperCase())
            setShowWarning(true)
        }
    }

    const CreateNewWard = async (code) => {
        const ward = {
            code: idWard,
            name: nameWard,
            districtCode: user,
            administrativeUnitId: idUnitWard
        }
        if (idWard !== '' && idUnitWard !== '' && nameWard !== "") {
            try {
                await axios.post("http://localhost:8080/api/v1/ward/save", ward, config)
                setWarningCreate(true)
                setShowEdit(false)
                const response = await axios('http://localhost:8080/api/v1/ward/by-district/' + user, config);
                setWards(response.data);
                setShow(false)
            } catch (error){
                let messageEdit = String(error.response.data)
                if (messageEdit.toUpperCase() === "ACCESS IS DENIED") setMessage("TÀI KHOẢN HIỆN KHÔNG CÓ QUYỀN THÊM MỚI")
                else setMessage(messageEdit.toUpperCase())
                setWarningCreate(true)
            }
        } else {
            setWarningCreate(true)
            setMessage("CHƯA NHẬP ĐỦ THÔNG TIN CẦN THIẾT 👿")
        }
    }

    useEffect(() => {
        fetchFullWard();
    }, [])

    const formatData = (wards) => {
        return wards.map((item) => ({ ...item, isActive: false }))
    }

    const CreateWard = () => {
        setShow(true)
        setIdWard('')
        setNameWard('')
        setIdUnitWard('')
    }

    const ModalWard = () => {
        return (
            <Modal show={show}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Khai báo Xã/Phường/Thị trấn</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên Xã/Phường/Thị trấn (*)</Form.Label>
                            <Form.Control
                                type="text"
                                value={nameWard}
                                onChange={(e) => {
                                    setNameWard(e.target.value)
                                    setWarningCreate(false)
                                }
                                }
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Cấp mã cho Xã/Phường/Thị trấn (**)</Form.Label>
                            <Form.Control
                                type="text"
                                value={idWard}
                                onChange={(e) => {
                                    setIdWard(e.target.value)
                                    setWarningCreate(false)
                                }}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select value={idUnitWard} onChange={(e) => { 
                                setIdUnitWard(e.target.value) 
                                setWarningCreate(false)}}><option></option><option value={8}>1. Phường</option><option value={9}>2. Thị trấn</option><option value={10}>3. Xã</option></Form.Select>
                        </Form.Group>
                    </Form>
                    {(showWarningCreate) ? <div className="noteWarning"><p>{message}</p></div> : null}
                </Modal.Body>

                <Modal.Footer>
                    <div className="note">
                        <p>(*) Tên phường/xã/thị trấn không được trùng lặp với tên phường/xã/thị trấn xã đã được khai báo trong đơn vị quản lý</p>
                        <p>(**) Mã số của phường/xã/thị trấn không được trùng lặp với mã số của phường/xã/thị trấn đã được khai báo</p>
                    </div>
                    <Button variant="secondary" onClick={() => { setShow(false) }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        CreateNewWard()
                    }}>
                        Xác nhận
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    const ModalEditWard = () => {
        return (
            <Modal show={showEdit}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Cập nhật thông tin của Phường/Xã/Thị trấn</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên Phường/Xã/Thị trấn</Form.Label>
                            <Form.Control
                                type="text"
                                value={nameWard}
                                onChange={(e) => {
                                    setNameWard(e.target.value)
                                    setShowWarning(false)
                                }
                                }
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Mã Phường/Xã/Thị trấn</Form.Label>
                            <Form.Control
                                type="number"
                                value={idWard}
                                onChange={(e) => {
                                    setIdWard(e.target.value)
                                    setShowWarning(false)
                                }}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Phân loại đơn vị hành chính</Form.Label>
                            <Form.Select value={idUnitWard} onChange={(e) => {
                                setIdUnitWard(e.target.value)
                                setShowWarning(false)
                            }}
                            >
                                <option></option><option value={8}>1. Phường</option><option value={9}>2. Thị trấn</option><option value={10}>3. Xã</option></Form.Select>
                        </Form.Group>
                    </Form>
                    {(showWarning) ? <div className="noteWarning"><p>{message}</p></div> : null}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShowEdit(false)
                        wards.map((item) => {
                            if (item.code === idWard) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        EditWard()
                        wards.map((item) => {
                            if (item.code === idWard) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Xác nhận
                    </Button>
                </Modal.Footer>
            </Modal >
        )
    }

    const listWards = wards.map((post) =>
        <tr key={post.code} value={post.code} onClick={() => {
            fetchDetailWard(post.code)
            setShowWarning(false)
            post.isActive = true
        }} className="rowTable" style={{ backgroundColor: (post.isActive) ? "yellow" : "white" }}>
            <td>{post.code}</td>
            <td>{post.name}</td>
            <td>{post.administrativeUnit.fullName}</td>
        </tr>
    );

    const TableResidential = () => {
        return (
            <div>
                <div>
                    <Table striped bordered hover size="sm" className="tableResidential">
                        <thead>
                            <tr>
                                <th>Mã</th>
                                <th>Tên đơn vị</th>
                                <th>Phân loại đơn vị hành chính</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listWards}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }


    return (
        <div>
            <NavbarPage />
            <Button className="buttonAdd" onClick={() => {
                setShow(true)
                setWarningCreate(false)
                CreateWard()
            }}>Khai báo xã/phường/thị trấn</Button>

            <div>
                <TableResidential />
                {(show) ? ModalWard() : null}
                {ModalEditWard()}
            </div>
        </div>
    );
}

export default Ward;
