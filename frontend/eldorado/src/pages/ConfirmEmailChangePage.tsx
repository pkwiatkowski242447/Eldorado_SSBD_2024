import {useNavigate, useParams} from 'react-router-dom';
import {api} from '@/api/api.ts';
import {useToast} from '@/components/ui/use-toast.ts';
import {Button} from "@/components/ui/button.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Card, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"
import {useAccount} from "@/hooks/useAccount.ts";

function ConfirmEmailChangePage() {
    const {token} = useParams<{ token: string }>();
    const decodedToken = decodeURIComponent(token!);
    const {toast} = useToast();
    const navigate = useNavigate();

    const {getCurrentAccount} = useAccount();

    function onClickButton() {
        console.log(decodedToken)
        if (localStorage.getItem('token')) {
            api.confirmEmail(decodedToken!)
                .then(() => {
                    toast({
                        title: "Email successfully changed!",
                        description: "Your email address has been successfully changed.",
                        action: (
                            <div>
                                <Button onClick={() => {
                                    navigate('/home', {replace: true});
                                    getCurrentAccount();
                                }}>
                                    Home
                                </Button>
                            </div>
                        ),
                    });
                })
                .catch((error) => {
                    toast({
                        title: "Error",
                        description: "There was an error changing your email address. Please try again later.",
                    });
                    console.log(error.response.data);
                });
        } else {
            api.confirmEmail(decodedToken!)
                .then(() => {
                    toast({
                        title: "Email successfully changed!",
                        description: "Your email address has been successfully changed.",
                        action: (
                            <div>
                                <Button onClick={() => {
                                    navigate('/login', {replace: true});
                                    getCurrentAccount();
                                }}>
                                    Log in
                                </Button>
                            </div>
                        ),
                    });
                })
                .catch((error) => {
                    toast({
                        title: "Error",
                        description: "There was an error changing your email address. Please try again later.",
                    });
                    console.log(error.response.data);
                });
        }
    }

    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
            <Card>
                <CardHeader>
                    <CardTitle>Almost there...</CardTitle>
                    <CardDescription>Confirm the change of your Eldorado email account by pressing the button below.</CardDescription>
                    <Button onClick={onClickButton} className='mx-auto h-auto w-auto'>Change email</Button>
                </CardHeader>
            </Card>
        </div>
    );
}

export default ConfirmEmailChangePage