import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Button from 'react-bootstrap/Button'
import './province.css'
import { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import Modal from 'react-bootstrap/Modal';
import { BiCheckCircle } from 'react-icons/bi'

function Hamlet() {
    const role_acc = JSON.parse(localStorage.getItem("user"));
    const user = role_acc.username;

    const [showWarning, setShowWarning] = useState(false)
    const [hamlets, setHamlets] = useState([]);
    const [show, setShow] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [defaultIdHamlet, setDefaultIdHamlet] = useState()
    const [nameHamlet, setNameHamlet] = useState('');
    const [idHamlet, setIdHamlet] = useState('');
    const [idUnitHamlet, setIdUnitHamlet] = useState('');
    const [checkedId, setCheckedId] = useState(-1);
    const [showWarningCreate, setWarningCreate] = useState(false);


    const fetchFullHamlet = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/hamlet/by-ward/' + user);
            setHamlets(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchDetailHamlet = async (code) => {
        setShowWarning(false)
        try {
            const response = await axios('http://localhost:8080/api/v1/hamlet/' + code);
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
            await axios.put("http://localhost:8080/api/v1/hamlet/save/" + defaultIdHamlet, hamlet)
            setShowWarning(false)
            setShowEdit(false)
            const response = await axios('http://localhost:8080/api/v1/hamlet/by-ward/' + user);
            setHamlets(response.data);
        } catch {
            setShowWarning(true)
        }
    }

    const CreateNewHamlet = async (code) => {
        const hamlet = {
            code: idHamlet,
            name: nameHamlet,
            wardCode: user,
            administrativeUnitId: Number(idUnitHamlet)
        }
        console.log(hamlet)
        if (checkedId === 1 && idHamlet !== '' && idUnitHamlet !== '') {
            try {
                await axios.post("http://localhost:8080/api/v1/hamlet/save", hamlet)
                setWarningCreate(true)
                setShowEdit(false)
                const response = await axios('http://localhost:8080/api/v1/hamlet/by-ward/' + user);
                setHamlets(response.data);
                setShow(false)
            } catch {
                setShowWarning(true)
            }
        } else {
            console.log('error')
            setWarningCreate(true)
        }
    }

    const CheckedIdNewHamlet = async () => {
        setCheckedId(0)
        if (idHamlet.substring(0, 6) !== user || idHamlet.length < 8) setCheckedId(3)
        else {
            try {
                const response = await axios.get("http://localhost:8080/api/v1/hamlet/" + idHamlet)
                setCheckedId(2)
            } catch {
                setCheckedId(1)
            }
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
        setIdHamlet()
        setNameHamlet()
        setIdUnitHamlet()
        setCheckedId(-1)
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
                                    setCheckedId(0)
                                    setWarningCreate(false)
                                    if (e.target.value.length === 0) setCheckedId(-1)
                                }}
                            />
                            {(checkedId === 0) ? <div className='checked' onClick={() => CheckedIdNewHamlet()}><BiCheckCircle className="iconChecked" />Kiểm tra</div> : null}
                            {(checkedId === 2) ? <div className="warningChecked">Mã của đơn vị hành chính vừa nhập đang bị trùng với một đơn vị hành chính đã có sẵn</div> : null}
                            {(checkedId === 3) ? <div className="warningChecked">Mã của đơn vị hành chính vừa nhập không nằm trong khu vực quản lý</div> : null}
                            {(checkedId === 1) ? <div className="successChecked">Bạn có thể sử dụng mã vừa nhập</div> : null}
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select value={idUnitHamlet} onChange={(e) => { 
                                setIdUnitHamlet(e.target.value) 
                                setWarningCreate(false)}}><option></option><option value={11}>1. Xóm</option><option value={12}>2. Thôn</option><option value={13}>3. Bản</option><option value={14}>4. Tổ dân phố</option></Form.Select>
                        </Form.Group>
                    </Form>
                    {(showWarningCreate) ? <div className="noteWarning"><p>THÊM MỚI KHÔNG THÀNH CÔNG</p></div> : null}
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
                        Lưu
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
                    {(showWarning) ? <div className="noteWarning"><p>THAY ĐỔI THÔNG TIN KHÔNG THÀNH CÔNG</p></div> : null}
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
                        setShowEdit(false)
                        hamlets.map((item) => {
                            if (item.code === idHamlet) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Lưu
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
            }}>+ Khai báo Xóm/Thôn/Bản/Tổ dân phố</Button>

            <div>
                <TableResidential />
                {(show) ? ModalHamlet() : null}
                {ModalEditHamlet()}
            </div>
        </div>
    );
}

export default Hamlet;
