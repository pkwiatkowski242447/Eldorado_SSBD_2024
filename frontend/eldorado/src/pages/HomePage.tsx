import SiteHeader from "@/components/SiteHeader.tsx";

function HomePage() {
    // const {account} = useAccountState();

    return (
        <div className="flex min-h-screen w-full flex-col">
            <SiteHeader/>
            {/*<div className="flex justify-between items-center mx-auto p-10">*/}
            {/*    <Card>*/}
            {/*        <CardHeader>*/}
            {/*            <CardTitle>Hello, {account?.login}</CardTitle>*/}
            {/*            <CardDescription>We can't offer you much (for now)</CardDescription>*/}
            {/*            <CardDescription>Here's some info about your account:</CardDescription>*/}
            {/*        </CardHeader>*/}
            {/*        <CardContent>*/}
            {/*            <p><strong>ID</strong> - {account?.id}</p>*/}
            {/*            <p><strong>Login</strong> - {account?.login}</p>*/}
            {/*            <p><strong>Name</strong> - {account?.name}</p>*/}
            {/*            <p><strong>Last Name</strong> - {account?.lastname}</p>*/}
            {/*            <p><strong>Email</strong> - {account?.email}</p>*/}
            {/*            <p><strong>Phone</strong> - {account?.phone}</p>*/}
            {/*            <p><strong>Account Language</strong> - {account?.accountLanguage}</p>*/}
            {/*            <p><strong>Active</strong> - {account?.active ? 'Yes' : 'No'}</p>*/}
            {/*            <p><strong>Blocked</strong> - {account?.blocked ? 'Yes' : 'No'}</p>*/}
            {/*            <p><strong>Verified</strong> - {account?.verified ? 'Yes' : 'No'}</p>*/}
            {/*            <p><strong>Version</strong> - {account?.version}</p>*/}
            {/*        </CardContent>*/}
            {/*    </Card>*/}
            {/*</div>*/}
        </div>
    )
}

export default HomePage