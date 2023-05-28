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

function Hamlet() {
    const user_account = JSON.parse(localStorage.getItem("user"));
    const user = user_account.username;
    const config = {
        headers: {
            Authorization: `Bearer ${user_account.accessToken}`
        },
    };

    const [showWarning, setShowWarning] = useState(false)
    const [hamlets, setHamlets] = useState([]);
    const [show, setShow] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [defaultIdHamlet, setDefaultIdHamlet] = useState()
    const [nameHamlet, setNameHamlet] = useState('');
    const [idHamlet, setIdHamlet] = useState('');
    const [idUnitHamlet, setIdUnitHamlet] = useState('');
    const [message, setMessage] = useState()
    const [showWarningCreate, setWarningCreate] = useState(false);

    const fetchFullHamlet = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/hamlet/by-ward/' + user, config);
            setHamlets(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchDetailHamlet = async (code) => {
        setShowWarning(false)
        try {
            const response = await axios('http://localhost:8080/api/v1/hamlet/' + code, config);
            setIdHamlet(response.data.code)
            setDefaultIdHamlet(response.data.code)
            setNameHamlet(response.data.name)
            setIdUnitHamlet(response.data.administrativeUnit.id)
            setShowEdit(true)
        } catch (err) {
            console.error(err);
        }
    };

    const EditHamlet = async () => {
        const hamlet = {
            code: idHamlet,
            name: nameHamlet,
            wardCode: user,
            administrativeUnitId: idUnitHamlet
        }
        console.log(hamlet)
        try {
            await axios.put("http://localhost:8080/api/v1/hamlet/save/" + defaultIdHamlet, hamlet, config)
            setShowWarning(false)
            setShowEdit(false)
            const response = await axios('http://localhost:8080/api/v1/hamlet/by-ward/' + user, config);
            setHamlets(response.data);
        } catch (error) {
            setShowWarning(true)
            setWarningCreate(true)
            let messageEdit = String(error.response.data)
            if (messageEdit.toUpperCase() === "ACCESS IS DENIED") setMessage("TÀI KHOẢN HIỆN KHÔNG CÓ QUYỀN CHỈNH SỬA")
            else setMessage(messageEdit.toUpperCase())
        }
    }

    const CreateNewHamlet = async () => {
        const hamlet = {
            code: idHamlet,
            name: nameHamlet,
            wardCode: user,
            administrativeUnitId: Number(idUnitHamlet)
        }
        console.log(hamlet)
        if (idHamlet !== '' && idUnitHamlet !== '' && idUnitHamlet !== '') {
            try {
                await axios.post("http://localhost:8080/api/v1/hamlet/save", hamlet, config)
                setWarningCreate(true)
                setShowEdit(false)
                const response = await axios('http://localhost:8080/api/v1/hamlet/by-ward/' + user, config);
                setHamlets(response.data);
                setShow(false)
            } catch (error) {
                setShowWarning(true)
                setWarningCreate(true)
                let messageEdit = String(error.response.data)
                if (messageEdit.toUpperCase() === "ACCESS IS DENIED") setMessage("TÀI KHOẢN HIỆN KHÔNG CÓ QUYỀN THÊM MỚI")
                else setMessage(messageEdit.toUpperCase())
            }
        } else {
            setMessage("CHƯA NHẬP ĐỦ THÔNG TIN CẦN THIẾT 👿")
            setWarningCreate(true)
        }
    }


    useEffect(() => {
        fetchFullHamlet();
    }, [])

    const formatData = (wards) => {
        return hamlets.map((item) => ({ ...item, isActive: false }))
    }

    const CreateHamlet = () => {
        setShow(true)
        setIdHamlet('')
        setNameHamlet('')
        setIdUnitHamlet('')
    }

    const ModalHamlet = () => {
        return (
            <Modal show={show}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Khai báo Xóm/Thôn/Bản/Tổ dân phố</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên Xóm/Thôn/Bản/Tổ dân phố (*)</Form.Label>
                            <Form.Control
                                type="text"
                                value={nameHamlet}
                                onChange={(e) => {
                                    setNameHamlet(e.target.value)
                                    setWarningCreate(false)
                                }
                                }
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Cấp mã cho Xóm/Thôn/Bản/Tổ dân phố (**)</Form.Label>
                            <Form.Control
                                type="text"
                                value={idHamlet}
                                onChange={(e) => {
                                    setIdHamlet(e.target.value)
                                    setWarningCreate(false)
                                }}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select value={idUnitHamlet} onChange={(e) => {
                                setIdUnitHamlet(e.target.value)
                                setWarningCreate(false)
                            }}><option></option><option value={11}>1. Xóm</option><option value={12}>2. Thôn</option><option value={13}>3. Bản</option><option value={14}>4. Tổ dân phố</option></Form.Select>
                        </Form.Group>
                    </Form>
                    {(showWarningCreate) ? <div className="noteWarning"><p>{message}</p></div> : null}
                </Modal.Body>

                <Modal.Footer>
                    <div className="note">
                        <p>(*) Tên xóm/thôn/bản/tổ dân phố không được trùng lặp với tên xóm/thôn/bản/tổ dân phố xã đã được khai báo trong đơn vị quản lý</p>
                        <p>(**) Mã số của xóm/thôn/bản/tổ dân phố không được trùng lặp với mã số của xóm/thôn/bản/tổ dân phố đã được khai báo</p>
                    </div>
                    <Button variant="secondary" onClick={() => { setShow(false) }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        CreateNewHamlet()
                    }}>
                        Xác nhận
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    const ModalEditHamlet = () => {
        return (
            <Modal show={showEdit}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Cập nhật thông tin của Xóm/Thôn/Bản/Tổ dân phố</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên Phường/Xã/Thị trấn</Form.Label>
                            <Form.Control
                                type="text"
                                value={nameHamlet}
                                onChange={(e) => {
                                    setNameHamlet(e.target.value)
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
                                value={idHamlet}
                                onChange={(e) => {
                                    setIdHamlet(e.target.value)
                                    setShowWarning(false)
                                }}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Phân loại đơn vị hành chính</Form.Label>
                            <Form.Select value={idUnitHamlet} onChange={(e) => {
                                setIdUnitHamlet(e.target.value)
                                setShowWarning(false)
                            }}
                            >
                                <option></option><option value={11}>1. Xóm</option><option value={12}>2. Thôn</option><option value={13}>3. Bản</option><option value={14}>4. Tổ dân phố</option></Form.Select>
                        </Form.Group>
                    </Form>
                    {(showWarning) ? <div className="noteWarning"><p>{message}</p></div> : null}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShowEdit(false)
                        hamlets.map((item) => {
                            if (item.code === idHamlet) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        EditHamlet()
                        hamlets.map((item) => {
                            if (item.code === idHamlet) {
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

    const listHamlets = hamlets.map((post) =>
        <tr key={post.code} value={post.code} onClick={() => {
            fetchDetailHamlet(post.code)
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
                            {listHamlets}
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
                CreateHamlet()
            }}>Khai báo Xóm/Thôn/Bản/Tổ dân phố</Button>

            <div>
                <TableResidential />
                {(show) ? ModalHamlet() : null}
                {ModalEditHamlet()}
            </div>
        </div>
    );
}

export default Hamlet;
