import React, {useState} from 'react';
import {useAccountState} from '../context/AccountContext';
import {RolesEnum} from '../types/TokenPayload';

function TestPage() {
    const {account, setAccount} = useAccountState();
    const [selectedRole, setSelectedRole] = useState(account?.activeRole);

    const handleRoleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedRole(event.target.value as RolesEnum);
        if (account) {
            setAccount({...account, activeRole: event.target.value as RolesEnum});
        }
    };

    const getBannerColor = (role: RolesEnum | undefined) => {
        switch (role) {
            case RolesEnum.ADMIN:
                return 'red';
            case RolesEnum.STAFF:
                return 'blue';
            case RolesEnum.CLIENT:
                return 'green';
            default:
                return 'gray';
        }
    };

    return (
        <div>
            <h1 style={{backgroundColor: getBannerColor(selectedRole)}}>Your selected role is: {selectedRole}</h1>
            {account && (
                <div>
                    <h2>User Information</h2>
                    <p>ID: {account.id}</p>
                    <p>Login: {account.login}</p>
                    <p>User Types: {account.userTypes.map((userType) => (
                        <p>{userType}</p>
                    ))}</p>
                    <p>Active Role: {account.activeRole}</p>
                    <div>
                        <h3>Change Active Role</h3>
                        <select value={selectedRole} onChange={handleRoleChange}>
                            {account.userTypes.map((role, index) => (
                                <option key={index} value={role}>
                                    {role}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>
            )}
        </div>
    )
}

export default TestPage