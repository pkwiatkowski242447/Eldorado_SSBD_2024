import {useAccountState} from '../context/AccountContext';
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import SiteHeader from "@/components/SiteHeader.tsx";

function HomePage() {
    const {account} = useAccountState();

    return (
        <div className="flex min-h-screen w-full flex-col">
            <SiteHeader/>
            <div className="flex justify-between items-center mx-auto">
                <Card>
                    <CardHeader>
                        <CardTitle>Hello, {account?.id}</CardTitle>
                        <CardDescription>We can't offer you much (for now)</CardDescription>
                        <CardDescription>Here's some info about your account:</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <p>ID - {account?.login}</p>
                    </CardContent>
                </Card>
            </div>

        </div>
    )
}

export default HomePage