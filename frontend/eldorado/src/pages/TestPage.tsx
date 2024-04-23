import React from 'react';
import {useAccountState} from '../context/AccountContext';

function TestPage() {
    const {account} = useAccountState();
    console.log(account)
    return (
        <div>
            <h1>This is a test page</h1>
            {account && (
                <div>
                    <h2>User Information</h2>
                    <p>ID: {account.id}</p>
                    <p>Login: {account.login}</p>
                    <p>User Types: {account.userTypes.map((userType) => (
                        <p>{userType}</p>
                    ))}</p>
                    <p>Active Role: {account.activeRole}</p>
                </div>
            )}
        </div>
    )
}

export default TestPage